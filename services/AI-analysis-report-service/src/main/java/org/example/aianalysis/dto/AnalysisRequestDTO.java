package org.example.aianalysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI分析请求DTO
 * 用于封装发送给大语言模型的数据
 */
public class AnalysisRequestDTO {
    @JsonProperty("current_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime currentTime;
    
    private String location;
    private WeatherInfo weather;
    @JsonProperty("recent_schedules")
    private List<SimplifiedSchedule> recentSchedules;
    
    // Getters and Setters
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }
    
    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public WeatherInfo getWeather() {
        return weather;
    }
    
    public void setWeather(WeatherInfo weather) {
        this.weather = weather;
    }
    
    public List<SimplifiedSchedule> getRecentSchedules() {
        return recentSchedules;
    }
    
    public void setRecentSchedules(List<SimplifiedSchedule> recentSchedules) {
        this.recentSchedules = recentSchedules;
    }
    
    /**
     * 天气信息
     */
    public static class WeatherInfo {
        private String today;
        private String tomorrow;
        
        public String getToday() {
            return today;
        }
        
        public void setToday(String today) {
            this.today = today;
        }
        
        public String getTomorrow() {
            return tomorrow;
        }
        
        public void setTomorrow(String tomorrow) {
            this.tomorrow = tomorrow;
        }
    }
    
    /**
     * 简化版日程信息
     */
    public static class SimplifiedSchedule {
        private Long id;
        private String time;
        private String title;
        private String type;
        private String priority;
        private List<String> tags;
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getTime() {
            return time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getPriority() {
            return priority;
        }
        
        public void setPriority(String priority) {
            this.priority = priority;
        }
        
        public List<String> getTags() {
            return tags;
        }
        
        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}