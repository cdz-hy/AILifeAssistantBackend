package org.example.dietrecord.dto;

import java.math.BigDecimal;

public class DietEntryCreateRequest {
    private String mealType; // breakfast, lunch, dinner, snack
    private String foodName; // 食物名称（用于搜索食物库）
    private Long foodItemId; // 食物库ID（如果已选择）
    private String customFoodName; // 自定义食物名称（如果食物库没有）
    private BigDecimal quantityG; // 食用重量（克）
    private String quantityDesc; // 食用描述（1个、半碗等）
    private String imageUrl; // 图片URL（如果有）

    // Getters and Setters
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public Long getFoodItemId() { return foodItemId; }
    public void setFoodItemId(Long foodItemId) { this.foodItemId = foodItemId; }

    public String getCustomFoodName() { return customFoodName; }
    public void setCustomFoodName(String customFoodName) { this.customFoodName = customFoodName; }

    public BigDecimal getQuantityG() { return quantityG; }
    public void setQuantityG(BigDecimal quantityG) { this.quantityG = quantityG; }

    public String getQuantityDesc() { return quantityDesc; }
    public void setQuantityDesc(String quantityDesc) { this.quantityDesc = quantityDesc; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

