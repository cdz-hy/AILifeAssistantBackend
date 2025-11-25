package org.example.dietrecord.mapper;

import org.apache.ibatis.annotations.*;
import org.example.dietrecord.entity.DietEntry;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietEntryMapper {

    // 根据餐次ID查找所有条目
    @Select("SELECT e.* FROM t_diet_log_entry e WHERE e.log_id = #{logId} ORDER BY e.id ASC")
    List<DietEntry> findByLogId(Long logId);

    // 根据用户ID和日期查找所有条目（需要关联餐次表）
    @Select("SELECT e.* FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date} " +
            "ORDER BY l.log_time ASC, e.id ASC")
    List<DietEntry> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 根据用户ID、日期和餐次类型查找条目
    @Select("SELECT e.* FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date} AND l.meal_type = #{mealType} " +
            "ORDER BY e.id ASC")
    List<DietEntry> findByUserIdAndDateAndMealType(@Param("userId") Long userId, 
                                                    @Param("date") LocalDate date,
                                                    @Param("mealType") String mealType);

    // 根据ID查找条目
    @Select("SELECT e.* FROM t_diet_log_entry e WHERE e.id = #{id}")
    DietEntry findById(Long id);

    // 插入新条目
    @Insert("INSERT INTO t_diet_log_entry (log_id, image_url, food_item_id, custom_food_name, " +
            "quantity_g, quantity_desc, calories_kcal) " +
            "VALUES (#{logId}, #{imageUrl}, #{foodItemId}, #{customFoodName}, " +
            "#{quantityG}, #{quantityDesc}, #{caloriesKcal})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DietEntry dietEntry);

    // 删除条目
    @Delete("DELETE FROM t_diet_log_entry WHERE id = #{id}")
    int delete(Long id);

    // 统计查询 - 总卡路里（需要关联食物库计算营养成分）
    @Select("SELECT COALESCE(SUM(e.calories_kcal), 0) FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date}")
    BigDecimal getTotalCaloriesByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 统计查询 - 总蛋白质（需要从食物库计算）
    @Select("SELECT COALESCE(SUM(CASE " +
            "WHEN e.food_item_id IS NOT NULL THEN (f.protein_per_100g * e.quantity_g / 100) " +
            "ELSE 0 END), 0) FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "LEFT JOIN t_food_item f ON e.food_item_id = f.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date}")
    BigDecimal getTotalProteinByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 统计查询 - 总脂肪
    @Select("SELECT COALESCE(SUM(CASE " +
            "WHEN e.food_item_id IS NOT NULL THEN (f.fat_per_100g * e.quantity_g / 100) " +
            "ELSE 0 END), 0) FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "LEFT JOIN t_food_item f ON e.food_item_id = f.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date}")
    BigDecimal getTotalFatByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 统计查询 - 总碳水化合物
    @Select("SELECT COALESCE(SUM(CASE " +
            "WHEN e.food_item_id IS NOT NULL THEN (f.carbs_per_100g * e.quantity_g / 100) " +
            "ELSE 0 END), 0) FROM t_diet_log_entry e " +
            "INNER JOIN t_diet_log l ON e.log_id = l.id " +
            "LEFT JOIN t_food_item f ON e.food_item_id = f.id " +
            "WHERE l.user_id = #{userId} AND DATE(l.log_time) = #{date}")
    BigDecimal getTotalCarbsByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}

