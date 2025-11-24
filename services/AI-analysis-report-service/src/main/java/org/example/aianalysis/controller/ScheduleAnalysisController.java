package org.example.aianalysis.controller;

import org.example.aianalysis.service.ScheduleAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 日程分析控制器
 * 处理日程分析相关的HTTP请求
 */
@RestController
@RequestMapping("/api/analysis")
public class ScheduleAnalysisController {
    
    @Autowired
    private ScheduleAnalysisService scheduleAnalysisService;
    
    /**
     * 分析用户日程并生成AI建议
     * @param userId 用户ID
     * @return AI分析结果
     */
    @GetMapping("/schedule/{userId}")
    public String analyzeSchedule(@PathVariable Long userId) {
        return scheduleAnalysisService.analyzeDailySchedule(userId);
    }
}