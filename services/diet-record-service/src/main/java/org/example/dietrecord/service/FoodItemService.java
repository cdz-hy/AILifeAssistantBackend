package org.example.dietrecord.service;

import org.example.dietrecord.entity.FoodItem;
import java.util.List;

public interface FoodItemService {
    FoodItem getFoodItemByName(String foodName);
    List<FoodItem> searchFoodItems(String foodName);
    FoodItem getFoodItemById(Long id);
    FoodItem createFoodItem(FoodItem foodItem);
    FoodItem updateFoodItem(FoodItem foodItem);
}

