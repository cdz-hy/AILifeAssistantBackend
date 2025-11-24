package org.example.aianalysis.mapper;

import org.apache.ibatis.annotations.*;
import org.example.aianalysis.entity.GeneratedReport;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GeneratedReportMapper {
    
    GeneratedReport selectByUserIdAndDate(@Param("userId") Long userId, @Param("reportType") String reportType, @Param("date") LocalDate date);
    
    void insert(GeneratedReport report);
    
    void update(GeneratedReport report);
    
    void deleteById(Long id);
    
    List<GeneratedReport> selectByUserIdAndDateRange(@Param("userId") Long userId, @Param("reportType") String reportType, 
                                                    @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}