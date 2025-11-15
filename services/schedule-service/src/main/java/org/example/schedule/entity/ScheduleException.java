package org.example.schedule.entity;

import java.time.LocalDateTime;

/**
 * 日程例外实体类
 * 用于处理重复日程的特殊情况，如修改或取消单次日程
 */
public class ScheduleException {
    private Long id;
    private Long scheduleId;
    private LocalDateTime originalStartTime;
    private Boolean isCancelled;
    private String newTitle;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
    
    // Constructors
    public ScheduleException() {}
    
    public ScheduleException(Long id, Long scheduleId, LocalDateTime originalStartTime, Boolean isCancelled,
                           String newTitle, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.originalStartTime = originalStartTime;
        this.isCancelled = isCancelled;
        this.newTitle = newTitle;
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    public LocalDateTime getOriginalStartTime() {
        return originalStartTime;
    }
    
    public void setOriginalStartTime(LocalDateTime originalStartTime) {
        this.originalStartTime = originalStartTime;
    }
    
    public Boolean getCancelled() {
        return isCancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
    
    public String getNewTitle() {
        return newTitle;
    }
    
    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }
    
    public LocalDateTime getNewStartTime() {
        return newStartTime;
    }
    
    public void setNewStartTime(LocalDateTime newStartTime) {
        this.newStartTime = newStartTime;
    }
    
    public LocalDateTime getNewEndTime() {
        return newEndTime;
    }
    
    public void setNewEndTime(LocalDateTime newEndTime) {
        this.newEndTime = newEndTime;
    }
}