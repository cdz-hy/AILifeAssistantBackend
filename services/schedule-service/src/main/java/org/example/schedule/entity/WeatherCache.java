package org.example.schedule.entity;

import java.time.LocalDateTime;

public class WeatherCache {
    private String cacheKey;
    private String dataJson;
    private LocalDateTime expiresAt;

    // Constructors
    public WeatherCache() {}

    public WeatherCache(String cacheKey, String dataJson, LocalDateTime expiresAt) {
        this.cacheKey = cacheKey;
        this.dataJson = dataJson;
        this.expiresAt = expiresAt;
    }

    // Getters and Setters
    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}