package org.example.dietrecord.service.impl;

import org.example.dietrecord.entity.FoodItem;
import org.example.dietrecord.mapper.FoodItemMapper;
import org.example.dietrecord.service.FoodItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemMapper foodItemMapper;

    public FoodItemServiceImpl(FoodItemMapper foodItemMapper) {
        this.foodItemMapper = foodItemMapper;
    }

    @Override
    public FoodItem getFoodItemByName(String foodName) {
        return foodItemMapper.findByName(foodName);
    }

    @Override
    public List<FoodItem> searchFoodItems(String foodName) {
        return foodItemMapper.searchByFoodName(foodName);
    }

    @Override
    public FoodItem getFoodItemById(Long id) {
        return foodItemMapper.findById(id);
    }

    @Override
    public FoodItem createFoodItem(FoodItem foodItem) {
        System.out.println("创建食物记录 - 食物名称: " + foodItem.getFoodName() + 
                ", 卡路里: " + foodItem.getCaloriesPer100g() + 
                ", 蛋白质: " + foodItem.getProteinPer100g() + 
                ", 脂肪: " + foodItem.getFatPer100g() + 
                ", 碳水: " + foodItem.getCarbsPer100g());
        foodItemMapper.insert(foodItem);
        FoodItem savedFoodItem = foodItemMapper.findById(foodItem.getId());
        System.out.println("食物记录创建成功 - foodItemId: " + savedFoodItem.getId() + 
                ", 保存后的营养成分 - 蛋白质: " + savedFoodItem.getProteinPer100g() + 
                ", 脂肪: " + savedFoodItem.getFatPer100g() + 
                ", 碳水: " + savedFoodItem.getCarbsPer100g());
        return savedFoodItem;
    }

    @Override
    public FoodItem updateFoodItem(FoodItem foodItem) {
        foodItemMapper.update(foodItem);
        return foodItemMapper.findById(foodItem.getId());
    }
}

