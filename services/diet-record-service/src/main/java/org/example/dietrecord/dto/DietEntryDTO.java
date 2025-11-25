package org.example.dietrecord.dto;

import java.math.BigDecimal;

public class DietEntryDTO {
    private Long id;
    private Long logId;
    private String mealType; // 从关联的 DietLog 获取
    private String foodName; // 食物名称（从 food_item 或 custom_food_name）
    private BigDecimal quantityG; // 食用重量（克）
    private String quantityDesc; // 食用描述
    private BigDecimal calories; // 卡路里
    private BigDecimal protein; // 蛋白质（从食物库计算）
    private BigDecimal fat; // 脂肪（从食物库计算）
    private BigDecimal carbs; // 碳水化合物（从食物库计算）
    private String imageUrl; // 图片URL

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public BigDecimal getQuantityG() { return quantityG; }
    public void setQuantityG(BigDecimal quantityG) { this.quantityG = quantityG; }

    public String getQuantityDesc() { return quantityDesc; }
    public void setQuantityDesc(String quantityDesc) { this.quantityDesc = quantityDesc; }

    public BigDecimal getCalories() { return calories; }
    public void setCalories(BigDecimal calories) { this.calories = calories; }
    public void setCaloriesKcal(BigDecimal caloriesKcal) { this.calories = caloriesKcal; } // 兼容方法

    public BigDecimal getProtein() { return protein; }
    public void setProtein(BigDecimal protein) { this.protein = protein; }

    public BigDecimal getFat() { return fat; }
    public void setFat(BigDecimal fat) { this.fat = fat; }

    public BigDecimal getCarbs() { return carbs; }
    public void setCarbs(BigDecimal carbs) { this.carbs = carbs; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

