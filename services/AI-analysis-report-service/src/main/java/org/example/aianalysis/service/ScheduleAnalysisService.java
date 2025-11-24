package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.dto.AnalysisRequestDTO;
import org.example.aianalysis.dto.AnalysisResultDTO;
import org.example.aianalysis.feign.ScheduleServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 日程分析服务
 * 负责从日程服务获取数据并进行AI分析
 */
@Service
public class ScheduleAnalysisService {
    
    @Autowired
    private ScheduleServiceClient scheduleServiceClient;
    
    @Autowired
    private LLMClient llmClient;
    
    @Autowired
    private NacosConfig nacosConfig;
    
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    /**
     * 分析用户日程并生成AI建议
     * @param userId 用户ID
     * @return AI分析结果
     */
    public String analyzeDailySchedule(Long userId) {
        try {
            // 1. 数据聚合
            LocalDateTime yesterday = LocalDateTime.now().minusDays(1).with(LocalTime.MIN);
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).with(LocalTime.MAX);
            
            // 获取用户日程数据
            String startTime = yesterday.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            String endTime = tomorrow.atZone(java.time.ZoneId.systemDefault()).toInstant().toString();
            
            System.out.println("正在获取用户 " + userId + " 的日程数据，时间范围: " + startTime + " 到 " + endTime);
            List<Map<String, Object>> schedules = scheduleServiceClient.getSchedulesByUserIdAndDateRange(
                    userId, startTime, endTime);
            System.out.println("获取到用户 " + userId + " 的日程数据，共 " + schedules.size() + " 条记录");
            
            // 获取天气数据
            String cityCode = nacosConfig.getDefaultCityCode();
            System.out.println("正在获取城市 " + cityCode + " 的天气数据");
            Map<String, Object> weather = scheduleServiceClient.getWeatherByCityCode(cityCode);
            System.out.println("获取到天气数据: " + weather);
            
            // 2. 组装 Prompt Data (DTO)
            AnalysisRequestDTO promptData = new AnalysisRequestDTO();
            promptData.setCurrentTime(LocalDateTime.now());
            promptData.setLocation("北京");
            
            // 处理天气信息
            AnalysisRequestDTO.WeatherInfo weatherInfo = new AnalysisRequestDTO.WeatherInfo();
            if (weather != null) {
                // 解析天气数据
                parseWeatherData(weatherInfo, weather);
            } else {
                // 默认天气信息
                weatherInfo.setToday("小雨转中雨, 10°C - 15°C, 空气优");
                weatherInfo.setTomorrow("晴朗, 5°C - 12°C");
            }
            promptData.setWeather(weatherInfo);
            
            // 处理日程信息
            List<AnalysisRequestDTO.SimplifiedSchedule> simplifiedSchedules = new ArrayList<>();
            for (Map<String, Object> scheduleMap : schedules) {
                AnalysisRequestDTO.SimplifiedSchedule simplifiedSchedule = new AnalysisRequestDTO.SimplifiedSchedule();
                
                // 提取日程基本信息
                simplifiedSchedule.setId(((Number) scheduleMap.get("id")).longValue());
                simplifiedSchedule.setTitle((String) scheduleMap.get("title"));
                simplifiedSchedule.setType(getTypeName(scheduleMap)); // 获取类型名称
                
                // 处理时间显示
                LocalDateTime startTimeObj = parseDateTime(scheduleMap.get("startTime"));
                LocalDateTime endTimeObj = parseDateTime(scheduleMap.get("endTime"));
                
                if (startTimeObj != null && endTimeObj != null) {
                    String timeDisplay = formatTimeDisplay(startTimeObj, endTimeObj);
                    simplifiedSchedule.setTime(timeDisplay);
                }
                
                // 处理优先级
                Boolean isUrgent = (Boolean) scheduleMap.get("urgent");
                Boolean isImportant = (Boolean) scheduleMap.get("important");
                String priority = getPriorityText(isUrgent, isImportant);
                simplifiedSchedule.setPriority(priority);
                
                // 特殊处理：如果日程是昨天且结束时间较晚，添加"熬夜"标签
                if (startTimeObj != null && isYesterday(startTimeObj)) {
                    if (endTimeObj != null && endTimeObj.getHour() >= 23) {
                        simplifiedSchedule.setTags(Arrays.asList("熬夜"));
                    }
                }
                
                simplifiedSchedules.add(simplifiedSchedule);
            }
            promptData.setRecentSchedules(simplifiedSchedules);
            
            // 3. 调用大模型
            String systemPrompt = getSystemPrompt();
            String userPrompt = objectMapper.writeValueAsString(promptData);
            
            System.out.println("=== 用户 " + userId + " 的日程分析 ===");
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
            System.err.println("分析用户日程时发生异常: " + e.getMessage());
            System.err.println("=== API 调用结果 ===");
            System.err.println("返回状态: 失败");
            System.err.println("错误信息:");
            e.printStackTrace();
            System.err.println("===================");
            return null;
        }
    }
    
    /**
     * 判断时间是否为昨天
     * @param time 时间
     * @return 是否为昨天
     */
    private boolean isYesterday(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1).with(LocalTime.MIN);
        LocalDateTime today = now.with(LocalTime.MIN);
        return time.isAfter(yesterday) && time.isBefore(today);
    }
    
    /**
     * 解析天气数据
     * @param weatherInfo 天气信息对象
     * @param weather 天气数据
     */
    private void parseWeatherData(AnalysisRequestDTO.WeatherInfo weatherInfo, Map<String, Object> weather) {
        try {
            // 从天气数据中提取信息
            String dataJson = (String) weather.get("dataJson");
            if (dataJson != null && !dataJson.isEmpty()) {
                // 解析JSON字符串
                JsonNode jsonNode = objectMapper.readTree(dataJson);
                JsonNode livesNode = jsonNode.get("lives");
                
                if (livesNode != null && livesNode.isArray() && livesNode.size() > 0) {
                    JsonNode liveData = livesNode.get(0);
                    String weatherStr = liveData.get("weather").asText();
                    String temperature = liveData.get("temperature").asText();
                    String humidity = liveData.get("humidity").asText();
                    
                    // 构造今日天气信息
                    String todayWeather = weatherStr + ", " + temperature + "°C" + 
                        (humidity != null && !humidity.isEmpty() ? ", 湿度" + humidity + "%" : "");
                    weatherInfo.setToday(todayWeather);
                    
                    // 明日天气使用默认值
                    weatherInfo.setTomorrow("晴朗, 5°C - 12°C");
                } else {
                    // 默认天气信息
                    weatherInfo.setToday("小雨转中雨, 10°C - 15°C, 空气优");
                    weatherInfo.setTomorrow("晴朗, 5°C - 12°C");
                }
            } else {
                // 默认天气信息
                weatherInfo.setToday("小雨转中雨, 10°C - 15°C, 空气优");
                weatherInfo.setTomorrow("晴朗, 5°C - 12°C");
            }
        } catch (Exception e) {
            System.err.println("解析天气数据时发生异常: " + e.getMessage());
            // 使用默认值
            weatherInfo.setToday("小雨转中雨, 10°C - 15°C, 空气优");
            weatherInfo.setTomorrow("晴朗, 5°C - 12°C");
        }
    }
    
    /**
     * 获取类型名称
     * @param scheduleMap 日程数据
     * @return 类型名称
     */
    private String getTypeName(Map<String, Object> scheduleMap) {
        // 尝试从type对象中获取typeName
        Object typeObj = scheduleMap.get("type");
        if (typeObj instanceof Map) {
            Map<String, Object> typeMap = (Map<String, Object>) typeObj;
            Object typeName = typeMap.get("typeName");
            if (typeName != null) {
                return typeName.toString();
            }
        }
        
        // 尝试直接获取typeName
        Object typeName = scheduleMap.get("typeName");
        if (typeName != null) {
            return typeName.toString();
        }
        
        // 默认返回"未分类"
        return "未分类";
    }
    
    /**
     * 解析日期时间
     * @param dateTimeObj 日期时间对象
     * @return LocalDateTime对象
     */
    private LocalDateTime parseDateTime(Object dateTimeObj) {
        if (dateTimeObj == null) {
            return null;
        }
        
        try {
            if (dateTimeObj instanceof String) {
                String dateTimeStr = (String) dateTimeObj;
                // 尝试不同的日期格式
                try {
                    return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                }
            }
        } catch (Exception e) {
            System.err.println("解析日期时间失败: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 格式化时间显示
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 格式化后的时间字符串
     */
    private String formatTimeDisplay(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime tomorrow = today.plusDays(1);
        
        String startDay = "";
        if (startTime.isEqual(yesterday) || (startTime.isAfter(yesterday) && startTime.isBefore(today))) {
            startDay = "昨天";
        } else if (startTime.isEqual(today) || (startTime.isAfter(today) && startTime.isBefore(tomorrow))) {
            startDay = "今天";
        } else if (startTime.isEqual(tomorrow) || (startTime.isAfter(tomorrow) && startTime.isBefore(tomorrow.plusDays(1)))) {
            startDay = "明天";
        } else {
            startDay = startTime.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
        
        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        
        return startDay + " " + startTimeStr + " - " + endTimeStr;
    }
    
    /**
     * 根据紧急重要性获取优先级文本
     * @param isUrgent 是否紧急
     * @param isImportant 是否重要
     * @return 优先级文本
     */
    private String getPriorityText(Boolean isUrgent, Boolean isImportant) {
        if (isUrgent != null && isUrgent && isImportant != null && isImportant) {
            return "非常重要";
        } else if (isUrgent != null && isUrgent) {
            return "紧急";
        } else if (isImportant != null && isImportant) {
            return "重要";
        } else {
            return "普通";
        }
    }
    
    /**
     * 获取系统提示词
     * @return 系统提示词
     */
    private String getSystemPrompt() {
        return "你是一位专业的\"个人时间管理与生活健康顾问\"。你的任务是根据用户的日程安排、当地天气和行为习惯，提供【今日】的行动建议。\n\n" +
                "分析逻辑与规则：\n" +
                "1. 【环境感知】：若今日或明日有恶劣天气（雨、雪、高温、重污染），检查是否有\"户外\"、\"运动\"、\"通勤\"类日程，并给出预警或调整建议。\n" +
                "2. 【精力管理】：检查\"昨天\"是否有结束时间晚于 23:00 的日程。若有，且\"今天\"上午有重要工作，建议适当推迟或插入休息。\n" +
                "3. 【冲突预判】：检查日程之间是否有缓冲时间不足的情况（尤其是地点不同的日程）。\n" +
                "4. 【前瞻准备】：若\"明天\"上午 10:00 前有\"重要\"日程，建议今天晚上预留准备时间。\n" +
                "5. 【语气风格】：亲切、简洁、行动导向。不要说废话，直接给建议。\n\n" +
                "输出格式要求：\n" +
                "必须严格返回 JSON 格式列表，不要包含 Markdown 标记或其他废话。格式如下：\n" +
                "[\n" +
                "  {\n" +
                "    \"type\": \"weather\" | \"energy\" | \"conflict\" | \"preparation\",\n" +
                "    \"risk_level\": \"high\" | \"medium\" | \"low\",\n" +
                "    \"title\": \"短标题 (不超过10字)\",\n" +
                "    \"content\": \"建议内容 (不超过50字，关键信息用 ** 加粗)\",\n" +
                "    \"related_schedule_id\": 123 (若有关联日程则填ID，否则null)\n" +
                "  }\n" +
                "]";
    }
}