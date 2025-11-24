package org.example.aianalysis.service;

import org.example.aianalysis.entity.AggDailyMetrics;

import java.time.LocalDate;

/**
 * 数据收集服务接口
 * 用于从其他微服务收集数据
 */
public interface DataCollectionService {
    
    /**
     * 收集指定用户和日期的所有相关数据
     * @param userId 用户ID
     * @param date 日期
     * @return 每日聚合指标
     */
    AggDailyMetrics collectDailyData(Long userId, LocalDate date);
    
    /**
     * 从日程服务收集数据
     * @param userId 用户ID
     * @param date 日期
     */
    void collectScheduleData(Long userId, LocalDate date);
    
    /**
     * 从财务服务收集数据
     * @param userId 用户ID
     * @param date 日期
     */
    void collectFinanceData(Long userId, LocalDate date);
    
    /**
     * 从饮食记录服务收集数据
     * @param userId 用户ID
     * @param date 日期
     */
    void collectDietData(Long userId, LocalDate date);
    
    /**
     * 从学习服务收集数据
     * @param userId 用户ID
     * @param date 日期
     */
    void collectStudyData(Long userId, LocalDate date);
}