package org.example.dietrecord.dto;

import java.util.List;

public class MealGroupDTO {
    private String mealType;
    private String titleZh;
    private String titleEn;
    private List<DietEntryDTO> items;

    public MealGroupDTO() {}

    public MealGroupDTO(String mealType, String titleZh, String titleEn) {
        this.mealType = mealType;
        this.titleZh = titleZh;
        this.titleEn = titleEn;
    }

    // Getters and Setters
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getTitleZh() { return titleZh; }
    public void setTitleZh(String titleZh) { this.titleZh = titleZh; }

    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }

    public List<DietEntryDTO> getItems() { return items; }
    public void setItems(List<DietEntryDTO> items) { this.items = items; }
}

