package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.feign.DietRecordServiceClient;
import org.example.aianalysis.feign.FinanceServiceClient;
import org.example.aianalysis.feign.ScheduleServiceClient;
import org.example.aianalysis.feign.StudyServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 综合分析服务
 * 负责整合各个服务的数据，进行跨领域的综合AI分析
 */
@Service
public class ComprehensiveAnalysisService {

    @Autowired
    private DietRecordServiceClient dietRecordServiceClient;

    @Autowired
    private FinanceServiceClient financeServiceClient;

    @Autowired
    private ScheduleServiceClient scheduleServiceClient;

    @Autowired
    private StudyServiceClient studyServiceClient;

    @Autowired
    private LLMClient llmClient;

    @Autowired
    private NacosConfig nacosConfig;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 生成每日综合分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return AI分析结果JSON字符串
     */
    public String generateDailyComprehensiveReport(Long userId, LocalDate date) {
        try {
            System.out.println("=== 开始生成用户 " + userId + " 在 " + date + " 的每日综合分析报告 ===");

            // 1. 并行获取各模块数据
            Map<String, Object> aggregatedData = collectAllData(userId, date);

            // 2. 构建AI提示词
            String systemPrompt = getSystemPrompt();
            String userPrompt = objectMapper.writeValueAsString(aggregatedData);

            System.out.println("\n=== 系统提示词 ===");
            System.out.println(systemPrompt);
            System.out.println("\n=== 用户数据 ===");
            System.out.println(userPrompt);

            // 3. 调用大模型进行综合分析
            String aiResponse = llmClient.chat(systemPrompt, userPrompt);

            System.out.println("\n=== AI分析结果 ===");
            System.out.println(aiResponse);
            System.out.println("=== 综合分析报告生成完成 ===\n");

            return aiResponse;

        } catch (Exception e) {
            System.err.println("生成每日综合分析报告时发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 收集所有模块的数据
     */
    private Map<String, Object> collectAllData(Long userId, LocalDate date) {
        Map<String, Object> data = new HashMap<>();
        data.put("date", date.format(DateTimeFormatter.ISO_DATE));

        // 获取天气数据
        data.put("weather", getWeatherData());

        // 获取日程数据
        data.put("schedule", getScheduleData(userId, date));

        // 获取财务数据
        data.put("finance", getFinanceData(userId, date));

        // 获取饮食数据
        data.put("diet", getDietData(userId, date));

        // 获取学习数据
        data.put("study", getStudyData(userId, date));

        return data;
    }

    /**
     * 获取天气数据
     */
    private Map<String, Object> getWeatherData() {
        Map<String, Object> weatherData = new HashMap<>();
        try {
            String cityCode = nacosConfig.getDefaultCityCode();
            System.out.println("正在获取城市 " + cityCode + " 的天气数据");
            Map<String, Object> weather = scheduleServiceClient.getWeatherByCityCode(cityCode);

            String dataJson = (String) weather.get("dataJson");
            if (dataJson != null && !dataJson.isEmpty()) {
                JsonNode jsonNode = objectMapper.readTree(dataJson);
                JsonNode livesNode = jsonNode.get("lives");

                if (livesNode != null && livesNode.isArray() && livesNode.size() > 0) {
                    JsonNode liveData = livesNode.get(0);
                    weatherData.put("condition", liveData.get("weather").asText());
                    weatherData.put("temp", liveData.get("temperature").asText() + "°C");
                    weatherData.put("humidity", liveData.get("humidity").asText() + "%");
                } else {
                    setDefaultWeather(weatherData);
                }
            } else {
                setDefaultWeather(weatherData);
            }
        } catch (Exception e) {
            System.err.println("获取天气数据失败: " + e.getMessage());
            setDefaultWeather(weatherData);
        }
        return weatherData;
    }

    private void setDefaultWeather(Map<String, Object> weatherData) {
        weatherData.put("condition", "未知");
        weatherData.put("temp", "20°C");
        weatherData.put("humidity", "50%");
    }

    /**
     * 获取日程数据
     */
    private Map<String, Object> getScheduleData(Long userId, LocalDate date) {
        Map<String, Object> scheduleData = new HashMap<>();
        try {
            System.out.println("正在获取用户 " + userId + " 的日程数据");

            // 获取昨日和今日的日程
            LocalDate yesterday = date.minusDays(1);
            LocalDateTime yesterdayStart = yesterday.atStartOfDay();
            LocalDateTime yesterdayEnd = yesterday.atTime(LocalTime.MAX);
            LocalDateTime todayStart = date.atStartOfDay();
            LocalDateTime todayEnd = date.atTime(LocalTime.MAX);

            String yesterdayStartStr = yesterdayStart.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String yesterdayEndStr = yesterdayEnd.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String todayStartStr = todayStart.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String todayEndStr = todayEnd.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();

            // 获取昨日日程（用于分析睡眠时间）
            List<Map<String, Object>> yesterdaySchedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, yesterdayStartStr, yesterdayEndStr);

            // 获取今日日程
            List<Map<String, Object>> todaySchedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, todayStartStr, todayEndStr);

            // 分析昨日睡眠时间（最后一条日程的结束时间）
            String sleepTime = "未知";
            if (yesterdaySchedules != null && !yesterdaySchedules.isEmpty()) {
                Map<String, Object> lastSchedule = yesterdaySchedules.get(yesterdaySchedules.size() - 1);
                Object endTimeObj = lastSchedule.get("endTime");
                if (endTimeObj != null) {
                    try {
                        LocalDateTime endTime = LocalDateTime.parse(endTimeObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                        sleepTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
            }
            scheduleData.put("sleep_time_yesterday", sleepTime);

            // 分析今日起床时间（第一条日程的开始时间）
            String wakeUpTime = "未知";
            if (todaySchedules != null && !todaySchedules.isEmpty()) {
                Map<String, Object> firstSchedule = todaySchedules.get(0);
                Object startTimeObj = firstSchedule.get("startTime");
                if (startTimeObj != null) {
                    try {
                        LocalDateTime startTime = LocalDateTime.parse(startTimeObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                        wakeUpTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
            }
            scheduleData.put("wake_up_today", wakeUpTime);

            // 分析今日日程密度和重要事项
            int totalSchedules = todaySchedules != null ? todaySchedules.size() : 0;
            List<String> importantEvents = new ArrayList<>();
            String busyLevel = "low";

            if (todaySchedules != null) {
                for (Map<String, Object> schedule : todaySchedules) {
                    Boolean isImportant = (Boolean) schedule.get("isImportant");
                    if (Boolean.TRUE.equals(isImportant)) {
                        String title = (String) schedule.get("title");
                        if (title != null) {
                            importantEvents.add(title);
                        }
                    }
                }

                if (totalSchedules >= 8) {
                    busyLevel = "high";
                } else if (totalSchedules >= 4) {
                    busyLevel = "medium";
                }
            }

            scheduleData.put("busy_level", busyLevel);
            scheduleData.put("important_events", importantEvents);
            scheduleData.put("total_schedules", totalSchedules);

        } catch (Exception e) {
            System.err.println("获取日程数据失败: " + e.getMessage());
            e.printStackTrace();
            scheduleData.put("sleep_time_yesterday", "未知");
            scheduleData.put("wake_up_today", "未知");
            scheduleData.put("busy_level", "unknown");
            scheduleData.put("important_events", new ArrayList<>());
        }
        return scheduleData;
    }

    /**
     * 获取财务数据
     */
    private Map<String, Object> getFinanceData(Long userId, LocalDate date) {
        Map<String, Object> financeData = new HashMap<>();
        try {
            System.out.println("正在获取用户 " + userId + " 的财务数据");

            // 获取最近交易记录
            List<Map<String, Object>> transactions = financeServiceClient.getRecentTransactions(userId);

            // 获取本月统计数据
            int year = date.getYear();
            int month = date.getMonthValue();
            Map<String, Object> monthlyStats = financeServiceClient.getMonthlyStats(userId, year, month);

            // 计算今日支出
            double dailyExpense = 0.0;
            String topCategory = "无";
            Map<String, Double> categoryExpenses = new HashMap<>();

            if (transactions != null) {
                for (Map<String, Object> transaction : transactions) {
                    Object transactionDateObj = transaction.get("transactionDate");
                    Object amountObj = transaction.get("amount");
                    String type = (String) transaction.get("type");

                    if (transactionDateObj != null && amountObj != null && "EXPENSE".equals(type)) {
                        try {
                            LocalDateTime transactionDate = LocalDateTime.parse(transactionDateObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                            if (transactionDate.toLocalDate().equals(date)) {
                                double amount = Double.parseDouble(amountObj.toString());
                                dailyExpense += amount;

                                // 统计分类
                                String categoryName = (String) transaction.get("categoryName");
                                if (categoryName != null) {
                                    categoryExpenses.put(categoryName, categoryExpenses.getOrDefault(categoryName, 0.0) + amount);
                                }
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
            }

            // 找出消费最多的分类
            if (!categoryExpenses.isEmpty()) {
                topCategory = categoryExpenses.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("无");
            }

            // 判断预算状态
            String budgetStatus = "normal";
            if (monthlyStats != null) {
                Object totalExpenseObj = monthlyStats.get("totalExpense");
                if (totalExpenseObj != null) {
                    double totalExpense = Double.parseDouble(totalExpenseObj.toString());
                    // 简单判断：如果月支出超过5000元，标记为warning
                    if (totalExpense > 5000) {
                        budgetStatus = "warning";
                    }
                }
            }

            financeData.put("daily_expense", dailyExpense);
            financeData.put("budget_status", budgetStatus);
            financeData.put("top_category", topCategory);

        } catch (Exception e) {
            System.err.println("获取财务数据失败: " + e.getMessage());
            e.printStackTrace();
            financeData.put("daily_expense", 0.0);
            financeData.put("budget_status", "unknown");
            financeData.put("top_category", "无");
        }
        return financeData;
    }

    /**
     * 获取饮食数据
     */
    private Map<String, Object> getDietData(Long userId, LocalDate date) {
        Map<String, Object> dietData = new HashMap<>();
        try {
            System.out.println("正在获取用户 " + userId + " 的饮食数据");

            String dateStr = date.format(DateTimeFormatter.ISO_DATE);

            // 获取营养统计
            Map<String, Object> nutritionStats = dietRecordServiceClient.getDailyNutritionStats(userId, dateStr);

            // 获取饮食日志
            Map<String, Object> dietLog = dietRecordServiceClient.getDietLog(userId, dateStr);

            // 分析热量状态
            String calorieStatus = "normal";
            if (nutritionStats != null) {
                Object totalCaloriesObj = nutritionStats.get("totalCalories");
                if (totalCaloriesObj != null) {
                    double totalCalories = Double.parseDouble(totalCaloriesObj.toString());
                    if (totalCalories < 1200) {
                        calorieStatus = "low";
                    } else if (totalCalories > 2500) {
                        calorieStatus = "high";
                    }
                }
            }

            // 检查是否有不规律进食（例如缺少早餐）
            boolean irregularMeal = false;
            List<String> notes = new ArrayList<>();

            if (dietLog != null && dietLog.get("entries") instanceof List) {
                List<Map<String, Object>> entries = (List<Map<String, Object>>) dietLog.get("entries");
                boolean hasBreakfast = entries.stream()
                        .anyMatch(entry -> "BREAKFAST".equals(entry.get("mealType")));

                if (!hasBreakfast && !entries.isEmpty()) {
                    irregularMeal = true;
                    notes.add("缺少早餐");
                }

                // 检查咖啡因摄入
                boolean hasCaffeine = entries.stream()
                        .anyMatch(entry -> {
                            String foodName = (String) entry.get("foodName");
                            String customFoodName = (String) entry.get("customFoodName");
                            String name = foodName != null ? foodName : customFoodName;
                            return name != null && (name.contains("咖啡") || name.contains("茶"));
                        });

                if (hasCaffeine) {
                    notes.add("摄入咖啡因");
                }
            }

            dietData.put("calorie_status", calorieStatus);
            dietData.put("irregular_meal", irregularMeal);
            dietData.put("notes", String.join("、", notes));

        } catch (Exception e) {
            System.err.println("获取饮食数据失败: " + e.getMessage());
            e.printStackTrace();
            dietData.put("calorie_status", "unknown");
            dietData.put("irregular_meal", false);
            dietData.put("notes", "");
        }
        return dietData;
    }

    /**
     * 获取学习数据
     */
    private Map<String, Object> getStudyData(Long userId, LocalDate date) {
        Map<String, Object> studyData = new HashMap<>();
        try {
            System.out.println("正在获取用户 " + userId + " 的学习数据");

            // 获取专注会话
            List<Map<String, Object>> focusSessions = studyServiceClient.getUserFocusSessions(userId);

            // 获取学习任务
            List<Map<String, Object>> studyTasks = studyServiceClient.getUserStudyTasks(userId);

            // 计算今日专注时长
            int focusMinutes = 0;
            List<String> focusTimeSlots = new ArrayList<>();

            if (focusSessions != null) {
                for (Map<String, Object> session : focusSessions) {
                    Object startTimeObj = session.get("startTime");
                    Object durationObj = session.get("actualDuration");

                    if (startTimeObj != null && durationObj != null) {
                        try {
                            LocalDateTime startTime = LocalDateTime.parse(startTimeObj.toString(), DateTimeFormatter.ISO_DATE_TIME);
                            if (startTime.toLocalDate().equals(date)) {
                                int duration = Integer.parseInt(durationObj.toString());
                                focusMinutes += duration;

                                String timeSlot = startTime.format(DateTimeFormatter.ofPattern("HH:mm")) +
                                        "-" + startTime.plusMinutes(duration).format(DateTimeFormatter.ofPattern("HH:mm"));
                                focusTimeSlots.add(timeSlot);
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
            }

            // 计算任务完成情况
            int tasksTotal = 0;
            int tasksCompleted = 0;

            if (studyTasks != null) {
                for (Map<String, Object> task : studyTasks) {
                    Object dueDateObj = task.get("dueDate");
                    if (dueDateObj != null) {
                        try {
                            LocalDate dueDate = LocalDate.parse(dueDateObj.toString(), DateTimeFormatter.ISO_DATE);
                            // 统计今日截止的任务
                            if (dueDate.equals(date) || dueDate.isBefore(date)) {
                                tasksTotal++;
                                Boolean isCompleted = (Boolean) task.get("isCompleted");
                                if (Boolean.TRUE.equals(isCompleted)) {
                                    tasksCompleted++;
                                }
                            }
                        } catch (Exception e) {
                            // 忽略解析错误
                        }
                    }
                }
            }

            studyData.put("focus_minutes", focusMinutes);
            studyData.put("tasks_completed", tasksCompleted);
            studyData.put("tasks_total", tasksTotal);
            studyData.put("focus_sessions", focusTimeSlots);

        } catch (Exception e) {
            System.err.println("获取学习数据失败: " + e.getMessage());
            e.printStackTrace();
            studyData.put("focus_minutes", 0);
            studyData.put("tasks_completed", 0);
            studyData.put("tasks_total", 0);
            studyData.put("focus_sessions", new ArrayList<>());
        }
        return studyData;
    }

    /**
     * 获取系统提示词
     */
    private String getSystemPrompt() {
        return "你是一位全能的\"AI 个人生活首席参谋\"。\n" +
                "你的任务是结合用户的【日程、财务、饮食、学习、天气】数据,进行跨领域的综合分析。\n" +
                "\n" +
                "请完成以下三个任务:\n" +
                "1. **打分**:计算今日/昨日的\"综合效率分\"(0.0-10.0)。\n" +
                "2. **洞察**:给出一口气的\"综合建议/洞察\"(不超过 40 字)。\n" +
                "3. **画像更新**:根据最近的行为数据,重新评估用户的\"AI 人格画像\"。\n" +
                "\n" +
                "【画像评估标准】:\n" +
                "- **核心效率时段**:根据学习模块的专注记录时间分布判断。\n" +
                "- **拖延指数**:根据学习任务的延期情况和日程的完成度判断 (0-1)。\n" +
                "- **消费习惯**:根据支出结构判断 (理性型/冲动型/保守型)。\n" +
                "- **饮食风险**:根据高糖高脂摄入及进食规律判断 (低/中/高)。\n" +
                "- **作息规律**:根据日程最早/最晚时间判断 (规律/熬夜/混乱)。\n" +
                "\n" +
                "【分析逻辑】:\n" +
                "- **学习 + 日程 = 拖延指数**:如果任务截止日期临近但专注记录很晚才开始,说明拖延。\n" +
                "- **学习 + 财务 = 压力消费**:如果学习完成度低,紧接着出现大额娱乐/购物支出,判定为压力型消费。\n" +
                "- **学习 + 饮食 = 能量供给**:如果有高强度学习计划但饮食热量不足,给出能量不足预警。\n" +
                "- **日程 + 饮食 + 天气**:根据日程强度、天气情况,建议合适的饮食调整。\n" +
                "\n" +
                "【综合效率分计算】:\n" +
                "Score = (日程完成率 * 0.3) + (专注时长达标率 * 0.3) + (预算健康度 * 0.2) + (饮食健康度 * 0.2)\n" +
                "\n" +
                "【输出格式】(必须为严格的 JSON 格式):\n" +
                "{\n" +
                "  \"dashboard_display\": {\n" +
                "    \"efficiency_score\": \"6.5\",\n" +
                "    \"score_trend\": \"down\",\n" +
                "    \"insight_title\": \"警惕压力型消费与拖延\",\n" +
                "    \"insight_content\": \"监测到您昨日任务完成率低且晚间才有专注记录,同时发生大额电子产品消费。建议通过运动缓解焦虑,而非购物。\"\n" +
                "  },\n" +
                "  \"user_persona_update\": {\n" +
                "    \"efficiency_peak_time\": \"20:00-21:00\",\n" +
                "    \"procrastination_index\": \"高 (0.85)\",\n" +
                "    \"spending_habit\": \"冲动型\",\n" +
                "    \"diet_risk\": \"中\",\n" +
                "    \"sleep_regularity\": \"较规律\"\n" +
                "  }\n" +
                "}";
    }

}
