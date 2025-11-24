package org.example.notification.dto;


import java.sql.Timestamp;

public class NotificationResponse {
    private Long id;
    private Long userId;
    private String content;
    private String channel;
    private String status;
    private String sourceEventType;
    private Timestamp createdAt;

    public NotificationResponse() {}

    public NotificationResponse(Long id, Long userId, String content, String channel,
                                String status, String sourceEventType, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.channel = channel;
        this.status = status;
        this.sourceEventType = sourceEventType;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSourceEventType() { return sourceEventType; }
    public void setSourceEventType(String sourceEventType) { this.sourceEventType = sourceEventType; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}