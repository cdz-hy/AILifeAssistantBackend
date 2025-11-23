package org.example.aianalysis.controller;

import org.example.aianalysis.entity.GeneratedReport;
import org.example.aianalysis.service.AIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * AI分析控制器
 * 处理AI分析相关的HTTP请求
 */
@RestController
@RequestMapping("/api/ai-analysis")
public class AIAnalysisController {
    
    @Autowired
    private AIAnalysisService aiAnalysisService;
    
    /**
     * 获取指定日期的日报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/daily/{userId}")
    public GeneratedReport getDailyReport(@PathVariable Long userId, 
                                         @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getDailyReportByDate(userId, targetDate);
        
        // 如果当日还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateDailyReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 获取指定日期的周报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/weekly/{userId}")
    public GeneratedReport getWeeklyReport(@PathVariable Long userId, 
                                          @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getWeeklyReportByDate(userId, targetDate);
        
        // 如果当周还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateWeeklyReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 获取指定日期的月报
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/monthly/{userId}")
    public GeneratedReport getMonthlyReport(@PathVariable Long userId, 
                                           @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getMonthlyReportByDate(userId, targetDate);
        
        // 如果当月还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateMonthlyReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 主动触发日报生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/daily/{userId}")
    public GeneratedReport generateDailyReport(@PathVariable Long userId,
                                              @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateDailyReport(userId, targetDate);
    }
    
    /**
     * 主动触发周报生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/weekly/{userId}")
    public GeneratedReport generateWeeklyReport(@PathVariable Long userId,
                                               @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateWeeklyReport(userId, targetDate);
    }
    
    /**
     * 主动触发月报生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/monthly/{userId}")
    public GeneratedReport generateMonthlyReport(@PathVariable Long userId,
                                                @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateMonthlyReport(userId, targetDate);
    }
}