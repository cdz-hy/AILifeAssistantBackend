package org.example.aianalysis.job;

import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.service.AIAnalysisService;
import org.example.aianalysis.service.DataCollectionService;
import org.example.aianalysis.service.ScheduleAnalysisService;
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
    
    @Autowired
    private ScheduleAnalysisService scheduleAnalysisService;
    
    @Autowired
    private NacosConfig nacosConfig;
    
    /**
     * 执行每日综合分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日综合分析")
    public void executeDailyComprehensiveAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日综合分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 1. 收集数据
        dataCollectionService.collectDailyData(userId, date);
        
        // 2. 生成报告
        aiAnalysisService.generateDailyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日综合分析任务执行完成");
    }
    
    /**
     * 执行每日日程分析任务（完整实现）
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日日程分析")
    public void executeDailyScheduleAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日日程分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成日程分析报告（完整实现）
        aiAnalysisService.generateDailyScheduleAnalysisReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日日程分析任务执行完成");
    }
    
    /**
     * 执行每日财务分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日财务分析")
    public void executeDailyFinanceAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日财务分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成财务分析报告（占位符）
        aiAnalysisService.generateDailyFinanceAnalysisReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日财务分析任务执行完成");
    }
    
    /**
     * 执行每日学习分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日学习分析")
    public void executeDailyStudyAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日学习分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成学习分析报告（占位符）
        aiAnalysisService.generateDailyStudyAnalysisReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日学习分析任务执行完成");
    }
    
    /**
     * 执行每日饮食分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每日饮食分析")
    public void executeDailyDietAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每日饮食分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成饮食分析报告（占位符）
        aiAnalysisService.generateDailyDietAnalysisReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每日饮食分析任务执行完成");
    }
    
    /**
     * 执行每周综合分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每周综合分析")
    public void executeWeeklyComprehensiveAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每周综合分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成周报
        aiAnalysisService.generateWeeklyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每周综合分析任务执行完成");
    }
    
    /**
     * 执行每月数据分析任务
     * @param userId 用户ID
     * @param date 日期
     */
    @Job(name = "执行每月数据分析")
    public void executeMonthlyAnalysis(Long userId, LocalDate date) {
        System.out.println("开始执行用户 " + userId + " 在 " + date + " 的每月数据分析任务");
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        System.out.println("从Nacos获取到的API配置 - URL: " + apiUrl + ", Key: " + apiKey);
        
        // 生成月报
        aiAnalysisService.generateMonthlyReport(userId, date);
        
        System.out.println("用户 " + userId + " 在 " + date + " 的每月数据分析任务执行完成");
    }
}