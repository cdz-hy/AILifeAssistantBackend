package org.example.dietrecord.controller;

import org.example.dietrecord.entity.FoodItem;
import org.example.dietrecord.service.FoodItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodItem>> searchFoodItems(@RequestParam String foodName) {
        try {
            List<FoodItem> items = foodItemService.searchFoodItems(foodName);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/name/{foodName}")
    public ResponseEntity<FoodItem> getFoodItemByName(@PathVariable String foodName) {
        try {
            FoodItem item = foodItemService.getFoodItemByName(foodName);
            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItem(@PathVariable Long id) {
        try {
            FoodItem item = foodItemService.getFoodItemById(id);
            if (item != null) {
                return ResponseEntity.ok(item);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
        try {
            FoodItem created = foodItemService.createFoodItem(foodItem);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

