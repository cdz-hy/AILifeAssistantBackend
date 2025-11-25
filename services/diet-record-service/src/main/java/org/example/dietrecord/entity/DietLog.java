package org.example.dietrecord.entity;

import java.time.LocalDateTime;

public class DietLog {
    private Long id;
    private Long userId;
    private String mealType; // breakfast, lunch, dinner, snack
    private LocalDateTime logTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public LocalDateTime getLogTime() { return logTime; }
    public void setLogTime(LocalDateTime logTime) { this.logTime = logTime; }
}

