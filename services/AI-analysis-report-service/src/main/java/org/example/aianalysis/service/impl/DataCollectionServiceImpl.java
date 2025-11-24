package org.example.aianalysis.service.impl;

import org.example.aianalysis.config.NacosConfig;
import org.example.aianalysis.entity.AggDailyMetrics;
import org.example.aianalysis.service.DataCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据收集服务实现类
 */
@Service
public class DataCollectionServiceImpl implements DataCollectionService {
    
    @Autowired
    private NacosConfig nacosConfig;
    
    @Override
    public AggDailyMetrics collectDailyData(Long userId, LocalDate date) {
        AggDailyMetrics metrics = new AggDailyMetrics();
        metrics.setUserId(userId);
        metrics.setDate(date);
        
        // 收集各类数据
        collectScheduleData(userId, date);
        collectFinanceData(userId, date);
        collectDietData(userId, date);
        collectStudyData(userId, date);
        
        // 设置默认值
        metrics.setTotalCalories(BigDecimal.ZERO);
        metrics.setTotalFocusMinutes(0);
        metrics.setTotalExpense(BigDecimal.ZERO);
        
        return metrics;
    }
    
    @Override
    public void collectScheduleData(Long userId, LocalDate date) {
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
        // 占位符：实际应调用日程服务API获取数据
        System.out.println("收集用户 " + userId + " 在 " + date + " 的日程数据");
        System.out.println("API地址: " + apiUrl + ", API Key: " + apiKey);
    }
    
    @Override
    public void collectFinanceData(Long userId, LocalDate date) {
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
        // 占位符：实际应调用财务服务API获取数据
        System.out.println("收集用户 " + userId + " 在 " + date + " 的财务数据");
        System.out.println("API地址: " + apiUrl + ", API Key: " + apiKey);
    }
    
    @Override
    public void collectDietData(Long userId, LocalDate date) {
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
        // 占位符：实际应调用饮食记录服务API获取数据
        System.out.println("收集用户 " + userId + " 在 " + date + " 的饮食数据");
        System.out.println("API地址: " + apiUrl + ", API Key: " + apiKey);
    }
    
    @Override
    public void collectStudyData(Long userId, LocalDate date) {
        // 从Nacos配置中心获取API地址和Key
        String apiUrl = nacosConfig.getAiModelApiUrl();
        String apiKey = nacosConfig.getAiModelApiKey();
        
        // 占位符：实际应调用学习服务API获取数据
        System.out.println("收集用户 " + userId + " 在 " + date + " 的学习数据");
        System.out.println("API地址: " + apiUrl + ", API Key: " + apiKey);
    }
}