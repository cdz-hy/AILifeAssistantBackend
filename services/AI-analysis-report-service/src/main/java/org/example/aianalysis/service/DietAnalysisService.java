package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.feign.DietRecordServiceClient;
import org.example.aianalysis.feign.ScheduleServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 饮食分析服务
 * 负责从饮食记录服务获取数据并结合日程、天气进行AI分析
 */
@Service
public class DietAnalysisService {

    @Autowired
    private DietRecordServiceClient dietRecordServiceClient;

    @Autowired
    private ScheduleServiceClient scheduleServiceClient;

    @Autowired
    private LLMClient llmClient;

    @Autowired
    private NacosConfig nacosConfig;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 分析用户饮食并生成AI建议
     * @param userId 用户ID
     * @return AI分析结果
     */
    public String analyzeDailyDiet(Long userId) {
        try {
            LocalDate today = LocalDate.now();
            String dateStr = today.format(DateTimeFormatter.ISO_DATE);

            // 1. 并行获取数据
            // 获取饮食日志
            System.out.println("正在获取用户 " + userId + " 的饮食日志，日期: " + dateStr);
            Map<String, Object> dietLog = dietRecordServiceClient.getDietLog(userId, dateStr);
            
            // 获取营养统计
            System.out.println("正在获取用户 " + userId + " 的营养统计，日期: " + dateStr);
            Map<String, Object> nutritionStats = dietRecordServiceClient.getDailyNutritionStats(userId, dateStr);

            // 获取日程数据 (今日)
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
            String startTime = startOfDay.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String endTime = endOfDay.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            
            System.out.println("正在获取用户 " + userId + " 的日程数据");
            List<Map<String, Object>> schedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, startTime, endTime);

            // 获取天气数据
            String cityCode = nacosConfig.getDefaultCityCode();
            System.out.println("正在获取城市 " + cityCode + " 的天气数据");
            Map<String, Object> weather = scheduleServiceClient.getWeatherByCityCode(cityCode);

            // 2. 数据清洗与组装
            Map<String, Object> context = new HashMap<>();
            context.put("date", dateStr);

            // 处理天气
            Map<String, String> weatherInfo = new HashMap<>();
            parseWeatherData(weatherInfo, weather);
            context.put("weather", weatherInfo);

            // 处理日程摘要
            Map<String, Object> scheduleSummary = analyzeSchedule(schedules);
            context.put("schedule_summary", scheduleSummary);

            // 处理饮食日志
            Map<String, Object> dietLogSummary = new HashMap<>();
            dietLogSummary.put("stats", nutritionStats);
            
            List<Map<String, Object>> meals = new ArrayList<>();
            if (dietLog != null && dietLog.get("entries") instanceof List) {
                List<Map<String, Object>> entries = (List<Map<String, Object>>) dietLog.get("entries");
                for (Map<String, Object> entry : entries) {
                    Map<String, Object> meal = new HashMap<>();
                    meal.put("type", entry.get("mealType"));
                    meal.put("time", entry.get("time")); // 假设有时间字段，或者根据mealType推断
                    
                    // 获取食物名称
                    String foodName = (String) entry.get("foodName");
                    if (foodName == null) {
                        Map<String, Object> foodItem = (Map<String, Object>) entry.get("foodItem");
                        if (foodItem != null) {
                            foodName = (String) foodItem.get("name");
                        } else {
                            foodName = (String) entry.get("customFoodName");
                        }
                    }
                    meal.put("food", Collections.singletonList(foodName));
                    meals.add(meal);
                }
            }
            dietLogSummary.put("meals", meals);
            context.put("diet_log", dietLogSummary);

            // 3. 调用大模型
            String systemPrompt = getSystemPrompt();
            String userPrompt = objectMapper.writeValueAsString(context);

            System.out.println("=== 用户 " + userId + " 的饮食分析 ===");
            System.out.println("系统提示词:");
            System.out.println(systemPrompt);
            System.out.println("\n用户提示词:");
            System.out.println(userPrompt);
            System.out.println("================================");

            String jsonResponse = llmClient.chat(systemPrompt, userPrompt);

            System.out.println("=== API 调用结果 ===");
            System.out.println("返回状态: 成功");
            System.out.println("返回数据:");
            System.out.println(jsonResponse);
            System.out.println("===================");

            return jsonResponse;

        } catch (Exception e) {
            System.err.println("分析用户饮食时发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析天气数据
     */
    private void parseWeatherData(Map<String, String> weatherInfo, Map<String, Object> weather) {
        try {
            String dataJson = (String) weather.get("dataJson");
            if (dataJson != null && !dataJson.isEmpty()) {
                JsonNode jsonNode = objectMapper.readTree(dataJson);
                JsonNode livesNode = jsonNode.get("lives");
                
                if (livesNode != null && livesNode.isArray() && livesNode.size() > 0) {
                    JsonNode liveData = livesNode.get(0);
                    weatherInfo.put("condition", liveData.get("weather").asText());
                    weatherInfo.put("temp", liveData.get("temperature").asText() + "°C");
                    weatherInfo.put("humidity", liveData.get("humidity").asText() + "%");
                } else {
                    setDefaultWeather(weatherInfo);
                }
            } else {
                setDefaultWeather(weatherInfo);
            }
        } catch (Exception e) {
            setDefaultWeather(weatherInfo);
        }
    }

    private void setDefaultWeather(Map<String, String> weatherInfo) {
        weatherInfo.put("condition", "未知");
        weatherInfo.put("temp", "20°C");
        weatherInfo.put("humidity", "50%");
    }

    /**
     * 分析日程摘要
     */
    private Map<String, Object> analyzeSchedule(List<Map<String, Object>> schedules) {
        Map<String, Object> summary = new HashMap<>();
        List<String> keywords = new ArrayList<>();
        int exerciseDuration = 0;
        boolean hasLateNight = false;
        boolean highMentalLoad = false;

        if (schedules != null) {
            for (Map<String, Object> schedule : schedules) {
                String title = (String) schedule.get("title");
                if (title != null) keywords.add(title);

                // 简单的关键词匹配来判断类型
                if (title != null) {
                    String lowerTitle = title.toLowerCase();
                    if (lowerTitle.contains("运动") || lowerTitle.contains("健身") || lowerTitle.contains("跑步")) {
                        // 估算时长，这里简单处理，假设每个日程1小时，实际应计算startTime和endTime差值
                        exerciseDuration += 60; 
                    }
                    if (lowerTitle.contains("会议") || lowerTitle.contains("学习") || lowerTitle.contains("代码")) {
                        highMentalLoad = true;
                    }
                }

                // 检查熬夜
                String endTimeStr = (String) schedule.get("endTime");
                if (endTimeStr != null) {
                     try {
                        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ISO_DATE_TIME); // 假设格式
                        if (endTime.getHour() >= 23 || endTime.getHour() < 5) {
                            hasLateNight = true;
                        }
                     } catch (Exception e) {
                         // ignore format error
                     }
                }
            }
        }

        if (highMentalLoad) {
            summary.put("intensity", "high_mental_load");
        } else if (exerciseDuration > 30) {
            summary.put("intensity", "high_physical_load");
        } else {
            summary.put("intensity", "moderate");
        }

        if (hasLateNight) {
            keywords.add("熬夜");
        }

        summary.put("keywords", keywords);
        summary.put("exercise_duration", exerciseDuration);

        return summary;
    }

    private String getSystemPrompt() {
        return "你是一位专业的“全科营养与生活平衡专家”。你的客户没有特定的减脂或增肌目标，你的职责是维护客户当下的**精力状态、免疫系统和长期健康**。\n" +
                "\n" +
                "请分析客户的【饮食记录】，并结合【天气】和【日程安排】，给出 3 条有深度的洞察或建议。\n" +
                "\n" +
                "分析原则：\n" +
                "1. **精力管理优先**：如果日程是高脑力消耗，警惕高碳水导致的“饭气攻心”；如果是高体力，关注能量补给。\n" +
                "2. **环境共鸣**：根据天气（冷热湿燥）建议饮食调整（如：寒冷雨天建议温补，炎热建议补水清淡）。\n" +
                "3. **成分陷阱**：识别深加工食品（如泡面、糖油混合物），温和提醒其对炎症或睡眠的影响。\n" +
                "4. **不评判热量**：不要纠结卡路里是否超标，而是关注“食物质量”和“搭配合理性”。\n" +
                "\n" +
                "返回格式（JSON）：\n" +
                "[\n" +
                "  {\n" +
                "    \"type\": \"energy\" | \"environment\" | \"nutrition\", \n" +
                "    \"title\": \"简短标题\",\n" +
                "    \"content\": \"温和且专业的建议内容\",\n" +
                "    \"related_meal\": \"lunch\" (可选)\n" +
                "  }\n" +
                "]";
    }
}
