package org.example.schedule.dto;

import org.example.schedule.util.RecurrencePatternConverter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程数据传输对象
 * 用于前后端数据传输
 */
public class ScheduleDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long typeId;
    private String typeName;
    private String typeColor;
    private Boolean isUrgent;
    private Boolean isImportant;
    private String recurrenceRule;
    private RecurrencePatternDTO recurrencePattern;
    private String status;
    private List<String> tags;
    private List<ScheduleReminderDTO> reminders;
    private List<ScheduleAISuggestionDTO> aiSuggestions;
    private List<ScheduleExceptionDTO> exceptions;
    
    // Constructors
    public ScheduleDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Long getTypeId() {
        return typeId;
    }
    
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeColor() {
        return typeColor;
    }
    
    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }
    
    public Boolean getUrgent() {
        return isUrgent;
    }
    
    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }
    
    public Boolean getImportant() {
        return isImportant;
    }
    
    public void setImportant(Boolean important) {
        isImportant = important;
    }
    
    public String getRecurrenceRule() {
        // 如果有RecurrencePatternDTO，则从它生成RRULE字符串
        if (recurrencePattern != null) {
            org.example.schedule.entity.RecurrencePattern pattern = RecurrencePatternConverter.toEntity(recurrencePattern);
            return org.example.schedule.util.RRuleUtils.toRRule(pattern);
        }
        return recurrenceRule;
    }
    
    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
        // 同时更新RecurrencePatternDTO
        if (recurrenceRule != null && !recurrenceRule.isEmpty()) {
            org.example.schedule.entity.RecurrencePattern pattern = org.example.schedule.util.RRuleUtils.fromRRule(recurrenceRule);
            this.recurrencePattern = RecurrencePatternConverter.toDTO(pattern);
        }
    }
    
    public RecurrencePatternDTO getRecurrencePattern() {
        // 如果还没有RecurrencePatternDTO但有recurrenceRule，则解析它
        if (recurrencePattern == null && recurrenceRule != null && !recurrenceRule.isEmpty()) {
            org.example.schedule.entity.RecurrencePattern pattern = org.example.schedule.util.RRuleUtils.fromRRule(recurrenceRule);
            recurrencePattern = RecurrencePatternConverter.toDTO(pattern);
        }
        return recurrencePattern;
    }
    
    public void setRecurrencePattern(RecurrencePatternDTO recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
        // 同时更新recurrenceRule字符串
        if (recurrencePattern != null) {
            org.example.schedule.entity.RecurrencePattern pattern = RecurrencePatternConverter.toEntity(recurrencePattern);
            this.recurrenceRule = org.example.schedule.util.RRuleUtils.toRRule(pattern);
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<ScheduleReminderDTO> getReminders() {
        return reminders;
    }
    
    public void setReminders(List<ScheduleReminderDTO> reminders) {
        this.reminders = reminders;
    }
    
    public List<ScheduleAISuggestionDTO> getAiSuggestions() {
        return aiSuggestions;
    }
    
    public void setAiSuggestions(List<ScheduleAISuggestionDTO> aiSuggestions) {
        this.aiSuggestions = aiSuggestions;
    }
    
    public List<ScheduleExceptionDTO> getExceptions() {
        return exceptions;
    }
    
    public void setExceptions(List<ScheduleExceptionDTO> exceptions) {
        this.exceptions = exceptions;
    }
}