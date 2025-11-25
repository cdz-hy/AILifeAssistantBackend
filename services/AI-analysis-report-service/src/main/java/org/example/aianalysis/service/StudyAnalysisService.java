package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.feign.ScheduleServiceClient;
import org.example.aianalysis.feign.StudyServiceClient;
import org.example.aianalysis.feign.DietRecordServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学习分析服务
 * 负责从学习服务获取数据并进行AI分析
 * 基于PDCA循环：Plan(计划) -> Do(执行) -> Check(检查) -> Act(改进)
 */
@Service
public class StudyAnalysisService {

    @Autowired
    private StudyServiceClient studyServiceClient;

    @Autowired
    private ScheduleServiceClient scheduleServiceClient;

    @Autowired
    private DietRecordServiceClient dietRecordServiceClient;

    @Autowired
    private LLMClient llmClient;

    @Autowired
    private NacosConfig nacosConfig;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 分析用户每日学习情况并生成AI建议
     * @param userId 用户ID
     * @return AI分析结果
     */
    public String analyzeDailyStudy(Long userId) {
        try {
            LocalDate today = LocalDate.now();
            System.out.println("=== 开始分析用户 " + userId + " 在 " + today + " 的学习情况 ===");

            // 1. 并行获取各模块数据
            Map<String, Object> studyContext = collectStudyData(userId, today);

            // 2. 构建AI提示词
            String systemPrompt = getSystemPrompt();
            String userPrompt = objectMapper.writeValueAsString(studyContext);

            System.out.println("\n=== 系统提示词 ===");
            System.out.println(systemPrompt);
            System.out.println("\n=== 学习数据 ===");
            System.out.println(userPrompt);

            // 3. 调用大模型进行分析
            String aiResponse = llmClient.chat(systemPrompt, userPrompt);

            System.out.println("\n=== AI分析结果 ===");
            System.out.println(aiResponse);
            System.out.println("=== 学习分析完成 ===\n");

            return aiResponse;

        } catch (Exception e) {
            System.err.println("分析用户学习情况时发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 收集学习相关的所有数据
     */
    private Map<String, Object> collectStudyData(Long userId, LocalDate date) {
        Map<String, Object> context = new HashMap<>();
        context.put("current_date", date.format(DateTimeFormatter.ISO_DATE));

        // 获取专注数据
        context.put("focus_stats", getFocusStats(userId, date));

        // 获取任务数据
        context.put("tasks", getTasksData(userId, date));

        // 获取外部环境数据
        context.put("context", getExternalContext(userId, date));

        return context;
    }

    /**
     * 获取专注统计数据
     */
    private Map<String, Object> getFocusStats(Long userId, LocalDate date) {
        Map<String, Object> focusStats = new HashMap<>();
        
        try {
            System.out.println("正在获取用户 " + userId + " 的专注会话数据");
            
            // 获取所有专注会话
            List<Map<String, Object>> allSessions = studyServiceClient.getUserFocusSessions(userId);
            
            // 过滤今日的专注会话
            List<Map<String, Object>> todaySessions = new ArrayList<>();
            int totalMinutes = 0;
            int interruptedCount = 0;
            List<Map<String, Object>> sessionDetails = new ArrayList<>();

            if (allSessions != null) {
                for (Map<String, Object> session : allSessions) {
                    Object startTimeObj = session.get("startTime");
                    if (startTimeObj != null) {
                        try {
                            LocalDateTime startTime = LocalDateTime.parse(startTimeObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                            
                            // 只统计今日的会话
                            if (startTime.toLocalDate().equals(date)) {
                                todaySessions.add(session);
                                
                                // 统计时长
                                Object durationObj = session.get("actualDuration");
                                if (durationObj != null) {
                                    int duration = Integer.parseInt(durationObj.toString());
                                    totalMinutes += duration;
                                    
                                    // 记录会话详情
                                    Map<String, Object> sessionDetail = new HashMap<>();
                                    sessionDetail.put("start", startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                                    sessionDetail.put("duration", duration);
                                    sessionDetails.add(sessionDetail);
                                }
                                
                                // 统计中断次数
                                String status = (String) session.get("status");
                                if ("INTERRUPTED".equals(status)) {
                                    interruptedCount++;
                                }
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
            }

            // 分析专注质量
            String focusQuality = "good";
            if (todaySessions.size() > 5 && totalMinutes / todaySessions.size() < 15) {
                focusQuality = "fragmented"; // 碎片化
            } else if (interruptedCount > todaySessions.size() / 2) {
                focusQuality = "interrupted"; // 频繁中断
            } else if (totalMinutes < 60) {
                focusQuality = "insufficient"; // 时长不足
            }

            focusStats.put("total_minutes", totalMinutes);
            focusStats.put("session_count", todaySessions.size());
            focusStats.put("sessions", sessionDetails);
            focusStats.put("interrupted_count", interruptedCount);
            focusStats.put("quality", focusQuality);
            focusStats.put("average_duration", todaySessions.isEmpty() ? 0 : totalMinutes / todaySessions.size());

        } catch (Exception e) {
            System.err.println("获取专注数据失败: " + e.getMessage());
            e.printStackTrace();
            focusStats.put("total_minutes", 0);
            focusStats.put("session_count", 0);
            focusStats.put("sessions", new ArrayList<>());
            focusStats.put("quality", "unknown");
        }

        return focusStats;
    }

    /**
     * 获取任务数据
     */
    private Map<String, Object> getTasksData(Long userId, LocalDate date) {
        Map<String, Object> tasksData = new HashMap<>();
        
        try {
            System.out.println("正在获取用户 " + userId + " 的学习任务数据");
            
            // 获取所有学习任务
            List<Map<String, Object>> allTasks = studyServiceClient.getUserStudyTasks(userId);
            
            List<Map<String, Object>> pendingTasks = new ArrayList<>();
            List<Map<String, Object>> completedTasks = new ArrayList<>();
            List<Map<String, Object>> overdueTasks = new ArrayList<>();
            int urgentCount = 0;

            if (allTasks != null) {
                for (Map<String, Object> task : allTasks) {
                    Boolean isCompleted = (Boolean) task.get("isCompleted");
                    Object dueDateObj = task.get("dueDate");
                    
                    if (dueDateObj != null) {
                        try {
                            LocalDate dueDate = LocalDate.parse(dueDateObj.toString(), DateTimeFormatter.ISO_DATE);
                            
                            Map<String, Object> taskInfo = new HashMap<>();
                            taskInfo.put("title", task.get("title"));
                            taskInfo.put("due", dueDate.format(DateTimeFormatter.ISO_DATE));
                            taskInfo.put("urgent", dueDate.equals(date) || dueDate.isBefore(date));
                            
                            if (Boolean.TRUE.equals(isCompleted)) {
                                // 已完成的任务
                                Object completedAtObj = task.get("completedAt");
                                if (completedAtObj != null) {
                                    try {
                                        LocalDateTime completedAt = LocalDateTime.parse(completedAtObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                                        if (completedAt.toLocalDate().equals(date)) {
                                            taskInfo.put("time", completedAt.format(DateTimeFormatter.ofPattern("HH:mm")));
                                            completedTasks.add(taskInfo);
                                        }
                                    } catch (Exception e) {
                                        // 忽略解析错误
                                    }
                                }
                            } else {
                                // 未完成的任务
                                if (dueDate.isBefore(date)) {
                                    // 逾期任务
                                    taskInfo.put("overdue_days", java.time.temporal.ChronoUnit.DAYS.between(dueDate, date));
                                    overdueTasks.add(taskInfo);
                                } else if (dueDate.equals(date)) {
                                    // 今日截止
                                    urgentCount++;
                                    pendingTasks.add(taskInfo);
                                } else if (dueDate.isBefore(date.plusDays(3))) {
                                    // 3天内截止
                                    pendingTasks.add(taskInfo);
                                }
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
            }

            tasksData.put("pending", pendingTasks);
            tasksData.put("completed", completedTasks);
            tasksData.put("overdue", overdueTasks);
            tasksData.put("urgent_count", urgentCount);
            tasksData.put("completion_rate", allTasks.isEmpty() ? 0 : 
                (double) completedTasks.size() / allTasks.size() * 100);

        } catch (Exception e) {
            System.err.println("获取任务数据失败: " + e.getMessage());
            e.printStackTrace();
            tasksData.put("pending", new ArrayList<>());
            tasksData.put("completed", new ArrayList<>());
            tasksData.put("overdue", new ArrayList<>());
            tasksData.put("urgent_count", 0);
        }

        return tasksData;
    }

    /**
     * 获取外部环境数据（日程、饮食等）
     */
    private Map<String, Object> getExternalContext(Long userId, LocalDate date) {
        Map<String, Object> context = new HashMap<>();
        
        try {
            // 获取今日日程密度
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            String startTime = startOfDay.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String endTime = endOfDay.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            
            List<Map<String, Object>> schedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, startTime, endTime);
            
            String scheduleDensity = "Low";
            if (schedules != null) {
                int count = schedules.size();
                if (count >= 8) {
                    scheduleDensity = "High";
                } else if (count >= 4) {
                    scheduleDensity = "Medium";
                }
            }
            context.put("schedule", scheduleDensity);

            // 获取饮食状态（判断是否高糖高碳水）
            String dateStr = date.format(DateTimeFormatter.ISO_DATE);
            Map<String, Object> nutritionStats = dietRecordServiceClient.getDailyNutritionStats(userId, dateStr);
            
            String dietState = "Normal";
            if (nutritionStats != null) {
                Object sugarObj = nutritionStats.get("totalSugar");
                Object carbsObj = nutritionStats.get("totalCarbs");
                
                if (sugarObj != null && carbsObj != null) {
                    try {
                        double sugar = Double.parseDouble(sugarObj.toString());
                        double carbs = Double.parseDouble(carbsObj.toString());
                        
                        if (sugar > 50 || carbs > 300) {
                            dietState = "High Sugar/Carbs Intake";
                        }
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
            }
            context.put("diet", dietState);

            // 获取睡眠质量（通过昨日最后一个日程推断）
            LocalDate yesterday = date.minusDays(1);
            LocalDateTime yesterdayStart = yesterday.atStartOfDay();
            LocalDateTime yesterdayEnd = yesterday.atTime(LocalTime.MAX);
            String yesterdayStartStr = yesterdayStart.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String yesterdayEndStr = yesterdayEnd.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            
            List<Map<String, Object>> yesterdaySchedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, yesterdayStartStr, yesterdayEndStr);
            
            String sleepQuality = "Unknown";
            if (yesterdaySchedules != null && !yesterdaySchedules.isEmpty()) {
                Map<String, Object> lastSchedule = yesterdaySchedules.get(yesterdaySchedules.size() - 1);
                Object endTimeObj = lastSchedule.get("endTime");
                if (endTimeObj != null) {
                    try {
                        LocalDateTime endTimeValue = LocalDateTime.parse(endTimeObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                        int hour = endTimeValue.getHour();
                        if (hour >= 23 || hour < 5) {
                            sleepQuality = "Poor Sleep (Late Night)";
                        } else {
                            sleepQuality = "Good Sleep";
                        }
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
            }
            context.put("sleep", sleepQuality);

        } catch (Exception e) {
            System.err.println("获取外部环境数据失败: " + e.getMessage());
            e.printStackTrace();
            context.put("schedule", "Unknown");
            context.put("diet", "Unknown");
            context.put("sleep", "Unknown");
        }

        return context;
    }

    /**
     * 获取系统提示词
     */
    private String getSystemPrompt() {
        return "你是一位专业的\"学习效能教练\"。你的目标是帮助用户建立心流，克服拖延，科学备考。\n" +
                "你需要根据用户的【专注记录】、【任务清单】和【外部环境】，对【今日】的学习状态进行诊断。\n" +
                "\n" +
                "分析维度（PDCA循环）：\n" +
                "1. **Plan（计划）**：检查今日任务的完成度与紧迫性，识别拖延迹象。\n" +
                "2. **Do（执行）**：评估专注时长是否达标，是否存在\"伪勤奋\"（如频繁中断、碎片化学习）。\n" +
                "3. **Check（检查）**：结合日程密度、饮食状态、睡眠质量，分析效率高/低的原因。\n" +
                "4. **Act（改进）**：给出具体可执行的改进建议。\n" +
                "\n" +
                "分析角度：\n" +
                "1. **拖延症精准打击**：如果有今日截止的任务未完成，且直到晚上才开始专注，给出警告。\n" +
                "2. **伪勤奋识别**：如果专注次数多但单次时长短（<15分钟），指出碎片化问题。\n" +
                "3. **状态归因与宽慰**：如果日程密度高或睡眠不足，给予理解和宽慰。\n" +
                "4. **高光时刻强化**：如果连续完成多个番茄钟，给予正向激励。\n" +
                "5. **方法论建议**：推荐\"5分钟起步法\"、\"番茄工作法\"、\"免打扰模式\"等具体技巧。\n" +
                "\n" +
                "输出要求：\n" +
                "返回 JSON 格式，包含 3 条核心建议。\n" +
                "格式：\n" +
                "[\n" +
                "  {\n" +
                "    \"type\": \"focus_praise\" | \"procrastination_alert\" | \"method_tip\" | \"empathy\" | \"fragmentation_warning\",\n" +
                "    \"title\": \"简短标题（带emoji）\",\n" +
                "    \"content\": \"详细分析和建议（50字以内）\",\n" +
                "    \"action\": \"具体可执行的行动建议\"\n" +
                "  }\n" +
                "]";
    }

}
