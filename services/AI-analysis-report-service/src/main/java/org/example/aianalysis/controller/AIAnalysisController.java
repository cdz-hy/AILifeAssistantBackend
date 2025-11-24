package org.example.aianalysis.controller;

import org.example.aianalysis.entity.GeneratedReport;
import org.example.aianalysis.service.AIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
     * 获取指定日期的日程分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/daily-schedule/{userId}")
    public GeneratedReport getDailyScheduleAnalysisReport(@PathVariable Long userId,
                                                        @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getDailyScheduleAnalysisReportByDate(userId, targetDate);
        
        // 如果当日还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateDailyScheduleAnalysisReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 获取指定日期的学习分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/daily-study/{userId}")
    public GeneratedReport getDailyStudyAnalysisReport(@PathVariable Long userId,
                                                     @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getDailyStudyAnalysisReportByDate(userId, targetDate);
        
        // 如果当日还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateDailyStudyAnalysisReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 获取指定日期的饮食分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/daily-diet/{userId}")
    public GeneratedReport getDailyDietAnalysisReport(@PathVariable Long userId,
                                                    @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getDailyDietAnalysisReportByDate(userId, targetDate);
        
        // 如果当日还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateDailyDietAnalysisReport(userId, targetDate);
        }
        
        return report;
    }
    
    /**
     * 获取指定日期的财务分析报告
     * @param userId 用户ID
     * @param date 日期
     * @return 分析报告
     */
    @GetMapping("/daily-finance/{userId}")
    public GeneratedReport getDailyFinanceAnalysisReport(@PathVariable Long userId,
                                                       @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        GeneratedReport report = aiAnalysisService.getDailyFinanceAnalysisReportByDate(userId, targetDate);
        
        // 如果当日还未分析，则进行分析
        if (report == null) {
            report = aiAnalysisService.generateDailyFinanceAnalysisReport(userId, targetDate);
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
        
        // 如果当日还未分析，则进行分析
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
        
        // 如果当日还未分析，则进行分析
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
    
    /**
     * 主动触发每日日程分析报告生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/daily-schedule/{userId}")
    public GeneratedReport generateDailyScheduleAnalysisReport(@PathVariable Long userId,
                                                              @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateDailyScheduleAnalysisReport(userId, targetDate);
    }
    
    /**
     * 主动触发每日学习分析报告生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/daily-study/{userId}")
    public GeneratedReport generateDailyStudyAnalysisReport(@PathVariable Long userId,
                                                           @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateDailyStudyAnalysisReport(userId, targetDate);
    }
    
    /**
     * 主动触发每日饮食分析报告生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/daily-diet/{userId}")
    public GeneratedReport generateDailyDietAnalysisReport(@PathVariable Long userId,
                                                          @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateDailyDietAnalysisReport(userId, targetDate);
    }
    
    /**
     * 主动触发每日财务分析报告生成
     * @param userId 用户ID
     * @param date 日期
     * @return 生成的报告
     */
    @PostMapping("/daily-finance/{userId}")
    public GeneratedReport generateDailyFinanceAnalysisReport(@PathVariable Long userId,
                                                             @RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        return aiAnalysisService.generateDailyFinanceAnalysisReport(userId, targetDate);
    }
}