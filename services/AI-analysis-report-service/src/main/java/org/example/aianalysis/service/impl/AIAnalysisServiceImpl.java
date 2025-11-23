package org.example.aianalysis.service.impl;

import org.example.aianalysis.entity.GeneratedReport;
import org.example.aianalysis.entity.AggDailyMetrics;
import org.example.aianalysis.mapper.GeneratedReportMapper;
import org.example.aianalysis.mapper.AggDailyMetricsMapper;
import org.example.aianalysis.service.AIAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * AI分析服务实现类
 */
@Service
public class AIAnalysisServiceImpl implements AIAnalysisService {
    
    @Autowired
    private GeneratedReportMapper generatedReportMapper;
    
    @Autowired
    private AggDailyMetricsMapper aggDailyMetricsMapper;
    
    @Override
    public GeneratedReport getDailyReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "daily", date);
    }
    
    @Override
    public GeneratedReport getWeeklyReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "weekly", date);
    }
    
    @Override
    public GeneratedReport getMonthlyReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "monthly", date);
    }
    
    @Override
    public GeneratedReport generateDailyReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = getDailyReportByDate(userId, date);
        if (existingReport != null) {
            return existingReport;
        }
        
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(java.time.LocalDateTime.now());
        
        // 占位符：实际应调用AI分析API
        report.setReportDataJson("{\"type\":\"daily\",\"date\":\"" + date + "\",\"summary\":\"今日分析报告占位符\"}");
        
        generatedReportMapper.insert(report);
        return report;
    }
    
    @Override
    public GeneratedReport generateWeeklyReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = getWeeklyReportByDate(userId, date);
        if (existingReport != null) {
            return existingReport;
        }
        
        // 计算一周的开始和结束日期
        LocalDate startDate = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate endDate = startDate.plusDays(6);
        
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("weekly");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setCreatedAt(java.time.LocalDateTime.now());
        
        // 占位符：实际应调用AI分析API
        report.setReportDataJson("{\"type\":\"weekly\",\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate + "\",\"summary\":\"本周分析报告占位符\"}");
        
        generatedReportMapper.insert(report);
        return report;
    }
    
    @Override
    public GeneratedReport generateMonthlyReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = getMonthlyReportByDate(userId, date);
        if (existingReport != null) {
            return existingReport;
        }
        
        // 计算一月的开始和结束日期
        LocalDate startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("monthly");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setCreatedAt(java.time.LocalDateTime.now());
        
        // 占位符：实际应调用AI分析API
        report.setReportDataJson("{\"type\":\"monthly\",\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate + "\",\"summary\":\"本月分析报告占位符\"}");
        
        generatedReportMapper.insert(report);
        return report;
    }
    
    @Override
    public List<AggDailyMetrics> getAggDailyMetrics(Long userId, LocalDate startDate, LocalDate endDate) {
        return aggDailyMetricsMapper.selectByUserIdAndDateRange(userId, startDate, endDate);
    }
    
    @Transactional
    @Override
    public AggDailyMetrics saveAggDailyMetrics(AggDailyMetrics metrics) {
        AggDailyMetrics existing = aggDailyMetricsMapper.selectByUserIdAndDate(metrics.getUserId(), metrics.getDate());
        if (existing != null) {
            metrics.setId(existing.getId());
            aggDailyMetricsMapper.update(metrics);
        } else {
            aggDailyMetricsMapper.insert(metrics);
        }
        return metrics;
    }
}