package org.example.dietrecord.entity;

import java.math.BigDecimal;

public class DietEntry {
    private Long id;
    private Long logId; // 关联的餐次ID
    private String imageUrl; // 用户上传的食物图片URL
    private Long foodItemId; // 关联到食物库的ID
    private String customFoodName; // 如果食物库没有, 用户自定义名称
    private BigDecimal quantityG; // 食用重量(克)
    private String quantityDesc; // 食用描述 (1个, 半碗)
    private BigDecimal caloriesKcal; // 冗余存储该条目的总卡路里

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getFoodItemId() { return foodItemId; }
    public void setFoodItemId(Long foodItemId) { this.foodItemId = foodItemId; }

    public String getCustomFoodName() { return customFoodName; }
    public void setCustomFoodName(String customFoodName) { this.customFoodName = customFoodName; }

    public BigDecimal getQuantityG() { return quantityG; }
    public void setQuantityG(BigDecimal quantityG) { this.quantityG = quantityG; }

    public String getQuantityDesc() { return quantityDesc; }
    public void setQuantityDesc(String quantityDesc) { this.quantityDesc = quantityDesc; }

    public BigDecimal getCaloriesKcal() { return caloriesKcal; }
    public void setCaloriesKcal(BigDecimal caloriesKcal) { this.caloriesKcal = caloriesKcal; }
}

