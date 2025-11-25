package org.example.notification.dto;

public class NotificationRequest {
    private Long userId;
    private String content;
    private String sourceEventType;
    private String priority;

    public NotificationRequest() {}

    public NotificationRequest(Long userId, String content, String sourceEventType, String priority) {
        this.userId = userId;
        this.content = content;
        this.sourceEventType = sourceEventType;
        this.priority = priority;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSourceEventType() { return sourceEventType; }
    public void setSourceEventType(String sourceEventType) { this.sourceEventType = sourceEventType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "userId=" + userId +
                ", content='" + content + '\'' +
                ", sourceEventType='" + sourceEventType + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}