package org.example.dietrecord.entity;

import java.math.BigDecimal;

public class FoodItem {
    private Long id;
    private String foodName;
    private BigDecimal caloriesPer100g; // 每100克的卡路里 (kcal)
    private BigDecimal proteinPer100g; // 每100克的蛋白质 (g)
    private BigDecimal fatPer100g; // 每100克的脂肪 (g)
    private BigDecimal carbsPer100g; // 每100克的碳水化合物 (g)
    private String createdBy; // 创建来源 (system, user)

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public BigDecimal getCaloriesPer100g() { return caloriesPer100g; }
    public void setCaloriesPer100g(BigDecimal caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }

    public BigDecimal getProteinPer100g() { return proteinPer100g; }
    public void setProteinPer100g(BigDecimal proteinPer100g) { this.proteinPer100g = proteinPer100g; }

    public BigDecimal getFatPer100g() { return fatPer100g; }
    public void setFatPer100g(BigDecimal fatPer100g) { this.fatPer100g = fatPer100g; }

    public BigDecimal getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(BigDecimal carbsPer100g) { this.carbsPer100g = carbsPer100g; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}

