package org.example.notification.entity;


import java.sql.Time;

public class NotificationDnd {
    private Long id;
    private Long userId;
    private Boolean isEnabled;
    private Time startTime;
    private Time endTime;

    public NotificationDnd() {}

    public NotificationDnd(Long id, Long userId, Boolean isEnabled, Time startTime, Time endTime) {
        this.id = id;
        this.userId = userId;
        this.isEnabled = isEnabled;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
}