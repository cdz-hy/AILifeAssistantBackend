package org.example.notification.entity;

public class NotificationPreference {
    private Long id;
    private Long userId;
    private String eventType;
    private String channel;
    private String alertType;
    private Boolean isEnabled;

    public NotificationPreference() {}

    public NotificationPreference(Long id, Long userId, String eventType,
                                  String channel, String alertType, Boolean isEnabled) {
        this.id = id;
        this.userId = userId;
        this.eventType = eventType;
        this.channel = channel;
        this.alertType = alertType;
        this.isEnabled = isEnabled;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public Boolean getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
}