package org.example.user.entity;

import java.time.LocalDateTime;

public class DataAuthorization {
    private Long id;
    private Long userId;
    private String dataType;
    private Boolean isAuthorized;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public Boolean getIsAuthorized() { return isAuthorized; }
    public void setIsAuthorized(Boolean isAuthorized) { this.isAuthorized = isAuthorized; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}