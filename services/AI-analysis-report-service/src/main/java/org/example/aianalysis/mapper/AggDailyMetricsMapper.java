package org.example.aianalysis.mapper;

import org.apache.ibatis.annotations.*;
import org.example.aianalysis.entity.AggDailyMetrics;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AggDailyMetricsMapper {
    
    @Select("SELECT * FROM t_agg_daily_metrics WHERE id = #{id}")
    AggDailyMetrics selectById(Long id);
    
    @Select("SELECT * FROM t_agg_daily_metrics WHERE user_id = #{userId} AND date = #{date}")
    AggDailyMetrics selectByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Select("SELECT * FROM t_agg_daily_metrics WHERE user_id = #{userId} AND date BETWEEN #{startDate} AND #{endDate}")
    List<AggDailyMetrics> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    @Insert("INSERT INTO t_agg_daily_metrics(user_id, date, total_calories, total_focus_minutes, total_expense) " +
            "VALUES(#{userId}, #{date}, #{totalCalories}, #{totalFocusMinutes}, #{totalExpense})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AggDailyMetrics metrics);
    
    @Update("UPDATE t_agg_daily_metrics SET total_calories = #{totalCalories}, total_focus_minutes = #{totalFocusMinutes}, " +
            "total_expense = #{totalExpense} WHERE id = #{id}")
    int update(AggDailyMetrics metrics);
    
    @Delete("DELETE FROM t_agg_daily_metrics WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM t_agg_daily_metrics WHERE user_id = #{userId} AND date = #{date}")
    int deleteByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}