package org.example.notification.dto;


public class PreferenceRequest {
    private String eventType;
    private String channel;
    private String alertType;
    private Boolean isEnabled;

    public PreferenceRequest() {}

    public PreferenceRequest(String eventType, String channel, String alertType, Boolean isEnabled) {
        this.eventType = eventType;
        this.channel = channel;
        this.alertType = alertType;
        this.isEnabled = isEnabled;
    }

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
}