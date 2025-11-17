package org.example.schedule.service.impl;

import org.example.schedule.entity.ScheduleAISuggestion;
import org.example.schedule.mapper.ScheduleAISuggestionMapper;
import org.example.schedule.service.AIRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AI推荐服务实现类
 * 实现AI建议相关业务逻辑
 */
@Service
public class AIRecommendationServiceImpl implements AIRecommendationService {
    
    @Autowired
    private ScheduleAISuggestionMapper scheduleAISuggestionMapper;
    
    @Override
    public ScheduleAISuggestion getAISuggestionById(Long id) {
        return scheduleAISuggestionMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleAISuggestion> getAISuggestionsByScheduleId(Long scheduleId) {
        return scheduleAISuggestionMapper.selectByScheduleId(scheduleId);
    }
    
    @Transactional
    @Override
    public ScheduleAISuggestion createAISuggestion(ScheduleAISuggestion aiSuggestion) {
        scheduleAISuggestionMapper.insert(aiSuggestion);
        return aiSuggestion;
    }
    
    @Transactional
    @Override
    public ScheduleAISuggestion updateAISuggestion(ScheduleAISuggestion aiSuggestion) {
        scheduleAISuggestionMapper.update(aiSuggestion);
        return aiSuggestion;
    }
    
    @Transactional
    @Override
    public void deleteAISuggestion(Long id) {
        scheduleAISuggestionMapper.deleteById(id);
    }
    
    @Transactional
    @Override
    public void deleteAISuggestionsByScheduleId(Long scheduleId) {
        scheduleAISuggestionMapper.deleteByScheduleId(scheduleId);
    }
}