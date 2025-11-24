package org.example.aianalysis.config;

import org.example.aianalysis.job.AIAnalysisJobHandler;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * AI分析调度配置类
 * 定期调度数据分析任务
 */
@Configuration
@EnableScheduling
public class AIAnalysisScheduler {
    
    @Autowired
    private JobScheduler jobScheduler;
    
    @Autowired
    private AIAnalysisJobHandler aiAnalysisJobHandler;
    
    // 模拟用户ID列表，实际应从用户服务获取
    private final List<Long> userIds = Arrays.asList(1L, 2L, 3L);
    
    /**
     * 每2小时执行一次日综合分析任务
     * 仅在用户活跃时间段执行（例如 8:00 - 22:00）
     */
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 每2小时执行一次
    public void scheduleDailyComprehensiveAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        // 只在用户活跃时间段执行
        if (now.getHour() >= 8 && now.getHour() <= 22) {
            LocalDate today = LocalDate.now();
            System.out.println("开始调度每日综合分析任务，时间：" + now);
            
            for (Long userId : userIds) {
                // 调度每日综合分析任务
                jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeDailyComprehensiveAnalysis(userId, today));
            }
            
            System.out.println("每日综合分析任务调度完成");
        }
    }
    
    /**
     * 每2小时执行一次日日程分析任务
     * 仅在用户活跃时间段执行（例如 8:00 - 22:00）
     */
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 每2小时执行一次
    public void scheduleDailyScheduleAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        // 只在用户活跃时间段执行
        if (now.getHour() >= 8 && now.getHour() <= 22) {
            LocalDate today = LocalDate.now();
            System.out.println("开始调度每日日程分析任务，时间：" + now);
            
            for (Long userId : userIds) {
                // 调度每日日程分析任务
                jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeDailyScheduleAnalysis(userId, today));
            }
            
            System.out.println("每日日程分析任务调度完成");
        }
    }
    
    /**
     * 每2小时执行一次日财务分析任务
     * 仅在用户活跃时间段执行（例如 8:00 - 22:00）
     */
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 每2小时执行一次
    public void scheduleDailyFinanceAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        // 只在用户活跃时间段执行
        if (now.getHour() >= 8 && now.getHour() <= 22) {
            LocalDate today = LocalDate.now();
            System.out.println("开始调度每日财务分析任务，时间：" + now);
            
            for (Long userId : userIds) {
                // 调度每日财务分析任务
                jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeDailyFinanceAnalysis(userId, today));
            }
            
            System.out.println("每日财务分析任务调度完成");
        }
    }
    
    /**
     * 每2小时执行一次日学习分析任务
     * 仅在用户活跃时间段执行（例如 8:00 - 22:00）
     */
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 每2小时执行一次
    public void scheduleDailyStudyAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        // 只在用户活跃时间段执行
        if (now.getHour() >= 8 && now.getHour() <= 22) {
            LocalDate today = LocalDate.now();
            System.out.println("开始调度每日学习分析任务，时间：" + now);
            
            for (Long userId : userIds) {
                // 调度每日学习分析任务
                jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeDailyStudyAnalysis(userId, today));
            }
            
            System.out.println("每日学习分析任务调度完成");
        }
    }
    
    /**
     * 每2小时执行一次日饮食分析任务
     * 仅在用户活跃时间段执行（例如 8:00 - 22:00）
     */
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000) // 每2小时执行一次
    public void scheduleDailyDietAnalysis() {
        LocalDateTime now = LocalDateTime.now();
        // 只在用户活跃时间段执行
        if (now.getHour() >= 8 && now.getHour() <= 22) {
            LocalDate today = LocalDate.now();
            System.out.println("开始调度每日饮食分析任务，时间：" + now);
            
            for (Long userId : userIds) {
                // 调度每日饮食分析任务
                jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeDailyDietAnalysis(userId, today));
            }
            
            System.out.println("每日饮食分析任务调度完成");
        }
    }
    
    /**
     * 每周一8点执行周综合分析任务
     */
    @Scheduled(cron = "0 0 8 * * MON") // 每周一8点
    public void scheduleWeeklyComprehensiveAnalysis() {
        LocalDate today = LocalDate.now();
        System.out.println("开始调度每周综合分析任务，日期：" + today);
        
        for (Long userId : userIds) {
            // 调度每周综合分析任务
            jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeWeeklyComprehensiveAnalysis(userId, today));
        }
        
        System.out.println("每周综合分析任务调度完成");
    }
    
    /**
     * 每月1号凌晨3点执行月报生成任务
     */
    @Scheduled(cron = "0 0 3 1 * ?") // 每月1号凌晨3点
    public void scheduleMonthlyAnalysis() {
        LocalDate today = LocalDate.now();
        System.out.println("开始调度每月数据分析任务，日期：" + today);
        
        for (Long userId : userIds) {
            // 调度每月数据分析任务
            jobScheduler.enqueue(() -> aiAnalysisJobHandler.executeMonthlyAnalysis(userId, today));
        }
        
        System.out.println("每月数据分析任务调度完成");
    }
}