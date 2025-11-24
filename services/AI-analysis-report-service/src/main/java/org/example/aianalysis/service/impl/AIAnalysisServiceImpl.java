package org.example.aianalysis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.dto.AnalysisRequestDTO;
import org.example.aianalysis.dto.AnalysisResultDTO;
import org.example.aianalysis.entity.GeneratedReport;
import org.example.aianalysis.entity.AggDailyMetrics;
import org.example.aianalysis.mapper.GeneratedReportMapper;
import org.example.aianalysis.mapper.AggDailyMetricsMapper;
import org.example.aianalysis.service.AIAnalysisService;
import org.example.aianalysis.service.LLMClient;
import org.example.aianalysis.service.ScheduleAnalysisService;
import org.example.aianalysis.service.FinanceAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    
    @Autowired
    private NacosConfig nacosConfig;
    
    @Autowired
    private LLMClient llmClient;
    
    @Autowired
    private ScheduleAnalysisService scheduleAnalysisService;
    
    @Autowired
    private FinanceAnalysisService financeAnalysisService;
    
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
    public GeneratedReport getDailyScheduleAnalysisReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "daily_schedule", date);
    }
    
    @Override
    public GeneratedReport getDailyStudyAnalysisReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "daily_study", date);
    }
    
    @Override
    public GeneratedReport getDailyDietAnalysisReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "daily_diet", date);
    }
    
    @Override
    public GeneratedReport getDailyFinanceAnalysisReportByDate(Long userId, LocalDate date) {
        return generatedReportMapper.selectByUserIdAndDate(userId, "daily_finance", date);
    }
    
    @Override
    public GeneratedReport generateDailyReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = getDailyReportByDate(userId, date);
        // 即使存在缓存也重新生成报告（每2小时强制更新）
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(java.time.LocalDateTime.now());
        
        // 占位符：实际应调用AI分析API
        report.setReportDataJson("{\"type\":\"daily\",\"date\":\"" + date + "\",\"summary\":\"今日综合分析报告占位符\",\"apiUrl\":\"" + apiUrl + "\",\"apiKey\":\"" + apiKey + "\"}");
        
        if (existingReport != null) {
            // 更新现有报告
            report.setId(existingReport.getId());
            generatedReportMapper.update(report);
        } else {
            // 插入新报告
            generatedReportMapper.insert(report);
        }
        return report;
    }
    
    @Override
    public GeneratedReport generateWeeklyReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = getWeeklyReportByDate(userId, date);
        if (existingReport != null) {
            return existingReport;
        }
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
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
        report.setReportDataJson("{\"type\":\"weekly\",\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate + "\",\"summary\":\"本周综合分析报告占位符\",\"apiUrl\":\"" + apiUrl + "\",\"apiKey\":\"" + apiKey + "\"}");
        
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
        
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
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
        report.setReportDataJson("{\"type\":\"monthly\",\"startDate\":\"" + startDate + "\",\"endDate\":\"" + endDate + "\",\"summary\":\"本月分析报告占位符\",\"apiUrl\":\"" + apiUrl + "\",\"apiKey\":\"" + apiKey + "\"}");
        
        generatedReportMapper.insert(report);
        return report;
    }
    
    @Override
    public GeneratedReport generateDailyScheduleAnalysisReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = generatedReportMapper.selectByUserIdAndDate(userId, "daily_schedule", date);
        // 即使存在缓存也重新生成报告（每2小时强制更新）
        
        // 调用日程分析服务获取分析结果（完整实现）
        String analysisResult = scheduleAnalysisService.analyzeDailySchedule(userId);
        
        // 生成新的报告
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily_schedule");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(LocalDateTime.now());
        report.setReportDataJson(analysisResult != null ? analysisResult : "{\"type\":\"daily_schedule\",\"date\":\"" + date + "\",\"summary\":\"今日日程分析报告占位符\"}");
        
        if (existingReport != null) {
            // 更新现有报告
            report.setId(existingReport.getId());
            generatedReportMapper.update(report);
        } else {
            // 插入新报告
            generatedReportMapper.insert(report);
        }
        return report;
    }
    
    @Override
    public GeneratedReport generateDailyStudyAnalysisReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = generatedReportMapper.selectByUserIdAndDate(userId, "daily_study", date);
        // 即使存在缓存也重新生成报告（每2小时强制更新）
        
        // TODO: 实现学习数据分析逻辑
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily_study");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(LocalDateTime.now());
        report.setReportDataJson("{\"type\":\"daily_study\",\"date\":\"" + date + "\",\"summary\":\"今日学习分析报告占位符\"}");
        
        if (existingReport != null) {
            // 更新现有报告
            report.setId(existingReport.getId());
            generatedReportMapper.update(report);
        } else {
            // 插入新报告
            generatedReportMapper.insert(report);
        }
        return report;
    }
    
    @Override
    public GeneratedReport generateDailyDietAnalysisReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = generatedReportMapper.selectByUserIdAndDate(userId, "daily_diet", date);
        // 即使存在缓存也重新生成报告（每2小时强制更新）
        
        // TODO: 实现饮食数据分析逻辑
        // 生成新的报告（使用占位符）
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily_diet");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(LocalDateTime.now());
        report.setReportDataJson("{\"type\":\"daily_diet\",\"date\":\"" + date + "\",\"summary\":\"今日饮食分析报告占位符\"}");
        
        if (existingReport != null) {
            // 更新现有报告
            report.setId(existingReport.getId());
            generatedReportMapper.update(report);
        } else {
            // 插入新报告
            generatedReportMapper.insert(report);
        }
        return report;
    }
    
    @Override
    public GeneratedReport generateDailyFinanceAnalysisReport(Long userId, LocalDate date) {
        // 检查是否已存在报告
        GeneratedReport existingReport = generatedReportMapper.selectByUserIdAndDate(userId, "daily_finance", date);
        // 即使存在缓存也重新生成报告（每2小时强制更新）
        
        // 实现财务数据分析逻辑
        String analysisResult = financeAnalysisService.analyzeDailyFinance(userId);
        
        // 生成新的报告
        GeneratedReport report = new GeneratedReport();
        report.setUserId(userId);
        report.setReportType("daily_finance");
        report.setStartDate(date);
        report.setEndDate(date);
        report.setCreatedAt(LocalDateTime.now());
        report.setReportDataJson(analysisResult != null ? analysisResult : "{\"type\":\"daily_finance\",\"date\":\"" + date + "\",\"summary\":\"今日财务分析报告占位符\"}");
        
        if (existingReport != null) {
            // 更新现有报告
            report.setId(existingReport.getId());
            generatedReportMapper.update(report);
        } else {
            // 插入新报告
            generatedReportMapper.insert(report);
        }
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