package org.example.schedule.service;

import org.example.schedule.entity.ScheduleAISuggestion;

import java.util.List;

/**
 * AI推荐服务接口
 * 负责AI建议相关业务逻辑
 */
public interface AIRecommendationService {
    
    /**
     * 根据ID获取AI建议
     * @param id 建议ID
     * @return AI建议实体
     */
    ScheduleAISuggestion getAISuggestionById(Long id);
    
    /**
     * 根据日程ID获取AI建议
     * @param scheduleId 日程ID
     * @return AI建议列表
     */
    List<ScheduleAISuggestion> getAISuggestionsByScheduleId(Long scheduleId);
    
    /**
     * 创建AI建议
     * @param aiSuggestion AI建议实体
     * @return 创建后的AI建议实体
     */
    ScheduleAISuggestion createAISuggestion(ScheduleAISuggestion aiSuggestion);
    
    /**
     * 更新AI建议
     * @param aiSuggestion AI建议实体
     * @return 更新后的AI建议实体
     */
    ScheduleAISuggestion updateAISuggestion(ScheduleAISuggestion aiSuggestion);
    
    /**
     * 删除AI建议
     * @param id 建议ID
     */
    void deleteAISuggestion(Long id);
    
    /**
     * 根据日程ID删除AI建议
     * @param scheduleId 日程ID
     */
    void deleteAISuggestionsByScheduleId(Long scheduleId);
}