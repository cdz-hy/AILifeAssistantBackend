package org.example.notification.entity;

import java.time.LocalDateTime;

/**
 * 通知日志实体类
 * 对应数据库表 t_notification_log
 */
public class NotificationLog {
    private Long id;
    private Long userId;
    private String content;
    private String channel;
    private String status;
    private String sourceEventType;
    private LocalDateTime createdAt;

    // Constructors
    public NotificationLog() {}

    public NotificationLog(Long id, Long userId, String content, String channel, 
                          String status, String sourceEventType, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.channel = channel;
        this.status = status;
        this.sourceEventType = sourceEventType;
        this.createdAt = createdAt;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceEventType() {
        return sourceEventType;
    }

    public void setSourceEventType(String sourceEventType) {
        this.sourceEventType = sourceEventType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}