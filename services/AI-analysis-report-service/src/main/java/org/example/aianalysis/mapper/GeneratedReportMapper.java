package org.example.aianalysis.mapper;

import org.apache.ibatis.annotations.*;
import org.example.aianalysis.entity.GeneratedReport;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GeneratedReportMapper {
    
    @Select("SELECT * FROM t_generated_report WHERE id = #{id}")
    GeneratedReport selectById(Long id);
    
    @Select("SELECT * FROM t_generated_report WHERE user_id = #{userId} AND report_type = #{reportType} " +
            "AND start_date <= #{targetDate} AND end_date >= #{targetDate} ORDER BY created_at DESC LIMIT 1")
    GeneratedReport selectByUserIdAndDate(@Param("userId") Long userId, 
                                         @Param("reportType") String reportType,
                                         @Param("targetDate") LocalDate targetDate);
    
    @Select("SELECT * FROM t_generated_report WHERE user_id = #{userId} AND report_type = #{reportType} " +
            "ORDER BY created_at DESC LIMIT 1")
    GeneratedReport selectLatestByUserIdAndType(@Param("userId") Long userId, 
                                               @Param("reportType") String reportType);
    
    @Select("SELECT * FROM t_generated_report WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<GeneratedReport> selectByUserId(Long userId);
    
    @Insert("INSERT INTO t_generated_report(user_id, report_type, start_date, end_date, report_data_json, created_at) " +
            "VALUES(#{userId}, #{reportType}, #{startDate}, #{endDate}, #{reportDataJson}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(GeneratedReport report);
    
    @Update("UPDATE t_generated_report SET report_data_json = #{reportDataJson}, created_at = #{createdAt} " +
            "WHERE id = #{id}")
    int update(GeneratedReport report);
    
    @Delete("DELETE FROM t_generated_report WHERE id = #{id}")
    int deleteById(Long id);
}