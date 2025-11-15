package org.example.schedule.dto;

import java.time.LocalDateTime;

/**
 * 日程例外数据传输对象
 * 用于前后端数据传输
 */
public class ScheduleExceptionDTO {
    private Long id;
    private Long scheduleId;
    private LocalDateTime originalStartTime;
    private Boolean isCancelled;
    private String newTitle;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
    
    // Constructors
    public ScheduleExceptionDTO() {}
    
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