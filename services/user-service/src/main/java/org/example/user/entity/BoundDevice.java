package org.example.user.entity;

import java.time.LocalDateTime;

public class BoundDevice {
    private Long id;
    private Long userId;
    private String deviceType;
    private String deviceToken;
    private LocalDateTime lastLoginAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getDeviceToken() { return deviceToken; }
    public void setDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}