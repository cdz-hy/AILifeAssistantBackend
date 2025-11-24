package org.example.user.entity;

import java.time.LocalDateTime;

public class PersonaMetric {
    private Long id;
    private Long userId;
    private String metricName;
    private String metricValue;
    private String lastUpdatedBy;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public String getMetricValue() { return metricValue; }
    public void setMetricValue(String metricValue) { this.metricValue = metricValue; }
    public String getLastUpdatedBy() { return lastUpdatedBy; }
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}