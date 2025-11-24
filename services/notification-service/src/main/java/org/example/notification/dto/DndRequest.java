package org.example.notification.dto;

import java.sql.Time;

public class DndRequest {
    private Boolean isEnabled;
    private Time startTime;
    private Time endTime;

    public DndRequest() {}

    public DndRequest(Boolean isEnabled, Time startTime, Time endTime) {
        this.isEnabled = isEnabled;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
}