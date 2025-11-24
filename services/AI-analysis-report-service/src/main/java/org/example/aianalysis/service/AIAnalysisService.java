package org.example.aianalysis.service;

import org.example.aianalysis.entity.GeneratedReport;
import org.example.aianalysis.entity.AggDailyMetrics;

import java.time.LocalDate;
import java.util.List;

/**
 * AI分析服务接口
 */
public interface AIAnalysisService {
    
    /**
     * 根据用户ID和日期获取最新的日报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getDailyReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的周报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getWeeklyReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的月报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getMonthlyReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的日程分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getDailyScheduleAnalysisReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的学习分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getDailyStudyAnalysisReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的饮食分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getDailyDietAnalysisReportByDate(Long userId, LocalDate date);
    
    /**
     * 根据用户ID和日期获取最新的财务分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    GeneratedReport getDailyFinanceAnalysisReportByDate(Long userId, LocalDate date);
    
    /**
     * 生成日报
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateDailyReport(Long userId, LocalDate date);
    
    /**
     * 生成周报
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateWeeklyReport(Long userId, LocalDate date);
    
    /**
     * 生成月报
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateMonthlyReport(Long userId, LocalDate date);
    
    /**
     * 生成每日日程分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateDailyScheduleAnalysisReport(Long userId, LocalDate date);
    
    /**
     * 生成每日学习分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateDailyStudyAnalysisReport(Long userId, LocalDate date);
    
    /**
     * 生成每日饮食分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateDailyDietAnalysisReport(Long userId, LocalDate date);
    
    /**
     * 生成每日财务分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    GeneratedReport generateDailyFinanceAnalysisReport(Long userId, LocalDate date);
    
    /**
     * 获取指定日期范围的每日聚合指标
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 聚合指标列表
     */
    List<AggDailyMetrics> getAggDailyMetrics(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 更新或创建每日聚合指标
     * @param metrics 聚合指标
     * @return 更新后的聚合指标
     */
    AggDailyMetrics saveAggDailyMetrics(AggDailyMetrics metrics);
}