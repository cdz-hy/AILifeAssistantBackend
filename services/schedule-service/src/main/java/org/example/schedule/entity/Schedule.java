package org.example.schedule.entity;

import org.example.schedule.util.RRuleUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程实体类
 * 对应数据库表 t_schedule
 */
public class Schedule {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long typeId;
    private Boolean isUrgent;
    private Boolean isImportant;
    private String recurrenceRule;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 添加RecurrencePattern支持
    private RecurrencePattern recurrencePattern;
    
    // 关联属性
    private ScheduleType type;
    private List<ScheduleTag> tags;
    private List<ScheduleReminder> reminders;
    private List<ScheduleAISuggestion> aiSuggestions;

    // Constructors
    public Schedule() {}

    public Schedule(Long id, Long userId, String title, String description, LocalDateTime startTime,
                   LocalDateTime endTime, Long typeId, Boolean isUrgent, Boolean isImportant,
                   String recurrenceRule, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.typeId = typeId;
        this.isUrgent = isUrgent;
        this.isImportant = isImportant;
        this.recurrenceRule = recurrenceRule;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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
        // 优先返回原始的recurrenceRule字段值
        if (recurrenceRule != null) {
            return recurrenceRule;
        }
        // 如果recurrenceRule为空但有RecurrencePattern，则从它生成RRULE字符串
        if (recurrencePattern != null) {
            return RRuleUtils.toRRule(recurrencePattern);
        }
        return null;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
        // 同时更新RecurrencePattern
        if (recurrenceRule != null && !recurrenceRule.isEmpty()) {
            this.recurrencePattern = RRuleUtils.fromRRule(recurrenceRule);
        }
    }
    
    // RecurrencePattern相关的getter和setter
    public RecurrencePattern getRecurrencePattern() {
        // 如果还没有RecurrencePattern但有recurrenceRule，则解析它
        if (recurrencePattern == null && recurrenceRule != null && !recurrenceRule.isEmpty()) {
            recurrencePattern = RRuleUtils.fromRRule(recurrenceRule);
        }
        return recurrencePattern;
    }
    
    public void setRecurrencePattern(RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
        // 同时更新recurrenceRule字符串
        if (recurrencePattern != null) {
            this.recurrenceRule = RRuleUtils.toRRule(recurrencePattern);
        } else {
            this.recurrenceRule = null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 关联属性的Getters和Setters
    public ScheduleType getType() {
        return type;
    }
    
    public void setType(ScheduleType type) {
        this.type = type;
    }
    
    public List<ScheduleTag> getTags() {
        return tags;
    }
    
    public void setTags(List<ScheduleTag> tags) {
        this.tags = tags;
    }
    
    public List<ScheduleReminder> getReminders() {
        return reminders;
    }
    
    public void setReminders(List<ScheduleReminder> reminders) {
        this.reminders = reminders;
    }
    
    public List<ScheduleAISuggestion> getAiSuggestions() {
        return aiSuggestions;
    }
    
    public void setAiSuggestions(List<ScheduleAISuggestion> aiSuggestions) {
        this.aiSuggestions = aiSuggestions;
    }

}