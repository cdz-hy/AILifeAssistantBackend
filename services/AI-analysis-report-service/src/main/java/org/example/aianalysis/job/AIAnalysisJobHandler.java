package org.example.aianalysis.job;

import org.example.aianalysis.service.AIAnalysisService;
import org.example.aianalysis.service.DataCollectionService;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * AI分析任务处理器
 * 负责处理JobRunr调度的任务，定期执行数据分析
 */
@Service
public class AIAnalysisJobHandler {
    
    @Autowired
    private DataCollectionService dataCollectionService;
    
    @Autowired
    private AIAnalysisService aiAnalysisService;
    
    /**
     * 执行每日数据分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日数据分析")
    public void executeDailyAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日数据分析任务");
        
        // 1. 收集数据
        dataCollectionService.collectDailyData(userId, date);
        
        // 2. 生成报告
        aiAnalysisService.generateDailyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日数据分析任务执行完成");
    }
    
    /**
     * 执行每周数据分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每周数据分析")
    public void executeWeeklyAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每周数据分析任务");
        
        // 生成周报
        aiAnalysisService.generateWeeklyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每周数据分析任务执行完成");
    }
    
    /**
     * 执行每月数据分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每月数据分析")
    public void executeMonthlyAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每月数据分析任务");
        
        // 生成月报
        aiAnalysisService.generateMonthlyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每月数据分析任务执行完成");
    }
}