package org.example.dietrecord.mapper;

import org.apache.ibatis.annotations.*;
import org.example.dietrecord.entity.FoodItem;
import java.util.List;

@Mapper
public interface FoodItemMapper {

    // 根据食物名称精确查找
    @Select("SELECT * FROM t_food_item WHERE food_name = #{foodName} LIMIT 1")
    FoodItem findByName(@Param("foodName") String foodName);

    // 根据食物名称模糊搜索
    @Select("SELECT * FROM t_food_item WHERE food_name LIKE CONCAT('%', #{foodName}, '%') LIMIT 10")
    List<FoodItem> searchByFoodName(@Param("foodName") String foodName);

    // 根据ID查找
    @Select("SELECT * FROM t_food_item WHERE id = #{id}")
    FoodItem findById(Long id);

    // 插入新食物
    @Insert("INSERT INTO t_food_item (food_name, calories_per_100g, protein_per_100g, fat_per_100g, " +
            "carbs_per_100g, created_by) " +
            "VALUES (#{foodName}, #{caloriesPer100g}, #{proteinPer100g}, #{fatPer100g}, " +
            "#{carbsPer100g}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FoodItem foodItem);

    // 更新食物信息
    @Update("UPDATE t_food_item SET calories_per_100g = #{caloriesPer100g}, " +
            "protein_per_100g = #{proteinPer100g}, fat_per_100g = #{fatPer100g}, " +
            "carbs_per_100g = #{carbsPer100g} WHERE id = #{id}")
    int update(FoodItem foodItem);
}

