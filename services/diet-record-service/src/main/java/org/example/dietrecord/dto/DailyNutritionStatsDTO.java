package org.example.dietrecord.dto;

import java.math.BigDecimal;

public class DailyNutritionStatsDTO {
    private BigDecimal totalCalories;
    private BigDecimal totalProtein;
    private BigDecimal totalFat;
    private BigDecimal totalCarbs;
    private BigDecimal targetCalories;
    private BigDecimal targetProtein;
    private BigDecimal targetFat;
    private BigDecimal targetCarbs;

    // Getters and Setters
    public BigDecimal getTotalCalories() { return totalCalories; }
    public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }

    public BigDecimal getTotalProtein() { return totalProtein; }
    public void setTotalProtein(BigDecimal totalProtein) { this.totalProtein = totalProtein; }

    public BigDecimal getTotalFat() { return totalFat; }
    public void setTotalFat(BigDecimal totalFat) { this.totalFat = totalFat; }

    public BigDecimal getTotalCarbs() { return totalCarbs; }
    public void setTotalCarbs(BigDecimal totalCarbs) { this.totalCarbs = totalCarbs; }

    public BigDecimal getTargetCalories() { return targetCalories; }
    public void setTargetCalories(BigDecimal targetCalories) { this.targetCalories = targetCalories; }

    public BigDecimal getTargetProtein() { return targetProtein; }
    public void setTargetProtein(BigDecimal targetProtein) { this.targetProtein = targetProtein; }

    public BigDecimal getTargetFat() { return targetFat; }
    public void setTargetFat(BigDecimal targetFat) { this.targetFat = targetFat; }

    public BigDecimal getTargetCarbs() { return targetCarbs; }
    public void setTargetCarbs(BigDecimal targetCarbs) { this.targetCarbs = targetCarbs; }
}

