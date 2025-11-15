package org.example.schedule.entity;

import java.time.LocalDateTime;

public class ScheduleReminder {
    private Long id;
    private Long scheduleId;
    private LocalDateTime remindAt;
    private String status;

    // Constructors
    public ScheduleReminder() {}

    public ScheduleReminder(Long id, Long scheduleId, LocalDateTime remindAt, String status) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.remindAt = remindAt;
        this.status = status;
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

    public LocalDateTime getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(LocalDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}