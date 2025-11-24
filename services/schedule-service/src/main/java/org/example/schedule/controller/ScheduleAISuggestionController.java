package org.example.schedule.controller;

import org.example.schedule.entity.ScheduleAISuggestion;
import org.example.schedule.service.AIRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日程AI建议控制器
 * 处理日程AI建议相关的HTTP请求
 */
@RestController
@RequestMapping("/api/schedules/suggestions")
public class ScheduleAISuggestionController {
    
    @Autowired
    private AIRecommendationService aiRecommendationService;
    
    /**
     * 根据ID获取AI建议
     * @param id 建议ID
     * @return AI建议实体
     */
    @GetMapping("/{id}")
    public ScheduleAISuggestion getAISuggestionById(@PathVariable Long id) {
        return aiRecommendationService.getAISuggestionById(id);
    }
    
    /**
     * 根据日程ID获取AI建议
     * @param scheduleId 日程ID
     * @return AI建议列表
     */
    @GetMapping("/schedule/{scheduleId}")
    public List<ScheduleAISuggestion> getAISuggestionsByScheduleId(@PathVariable Long scheduleId) {
        return aiRecommendationService.getAISuggestionsByScheduleId(scheduleId);
    }
    
    /**
     * 创建AI建议
     * @param aiSuggestion AI建议实体
     * @return 创建后的AI建议实体
     */
    @PostMapping
    public ScheduleAISuggestion createAISuggestion(@RequestBody ScheduleAISuggestion aiSuggestion) {
        return aiRecommendationService.createAISuggestion(aiSuggestion);
    }
    
    /**
     * 更新AI建议
     * @param id 建议ID
     * @param aiSuggestion AI建议实体
     * @return 更新后的AI建议实体
     */
    @PutMapping("/{id}")
    public ScheduleAISuggestion updateAISuggestion(@PathVariable Long id, @RequestBody ScheduleAISuggestion aiSuggestion) {
        aiSuggestion.setId(id);
        return aiRecommendationService.updateAISuggestion(aiSuggestion);
    }
    
    /**
     * 删除AI建议
     * @param id 建议ID
     */
    @DeleteMapping("/{id}")
    public void deleteAISuggestion(@PathVariable Long id) {
        aiRecommendationService.deleteAISuggestion(id);
    }
}