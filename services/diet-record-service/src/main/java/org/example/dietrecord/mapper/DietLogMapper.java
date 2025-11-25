package org.example.dietrecord.mapper;

import org.apache.ibatis.annotations.*;
import org.example.dietrecord.entity.DietLog;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietLogMapper {

    // 根据用户ID和日期查找所有餐次
    @Select("SELECT * FROM t_diet_log WHERE user_id = #{userId} AND DATE(log_time) = #{date} ORDER BY log_time ASC")
    List<DietLog> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 根据用户ID、日期和餐次类型查找餐次
    @Select("SELECT * FROM t_diet_log WHERE user_id = #{userId} AND DATE(log_time) = #{date} AND meal_type = #{mealType} LIMIT 1")
    DietLog findByUserIdAndDateAndMealType(@Param("userId") Long userId, 
                                           @Param("date") LocalDate date,
                                           @Param("mealType") String mealType);
    
    // 调试方法：查询所有餐次记录
    @Select("SELECT * FROM t_diet_log WHERE user_id = #{userId} AND DATE(log_time) = #{date}")
    List<DietLog> findAllByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // 根据ID查找餐次
    @Select("SELECT * FROM t_diet_log WHERE id = #{id}")
    DietLog findById(Long id);

    // 插入新餐次
    @Insert("INSERT INTO t_diet_log (user_id, meal_type, log_time) " +
            "VALUES (#{userId}, #{mealType}, #{logTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DietLog dietLog);

    // 删除餐次（会级联删除条目）
    @Delete("DELETE FROM t_diet_log WHERE id = #{id}")
    int delete(Long id);
}

