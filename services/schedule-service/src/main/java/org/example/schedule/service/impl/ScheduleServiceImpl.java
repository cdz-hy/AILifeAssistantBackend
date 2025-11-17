package org.example.schedule.service.impl;

import org.example.schedule.entity.*;
import org.example.schedule.mapper.*;
import org.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程服务实现类
 * 实现日程管理相关业务逻辑
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    private ScheduleTypeMapper scheduleTypeMapper;
    
    @Autowired
    private ScheduleTagMapper scheduleTagMapper;
    
    @Autowired
    private ScheduleTagMapMapper scheduleTagMapMapper;
    
    @Autowired
    private ScheduleReminderMapper scheduleReminderMapper;
    
    @Autowired
    private ScheduleAISuggestionMapper scheduleAISuggestionMapper;
    
    @Autowired
    private WeatherCacheMapper weatherCacheMapper;
    
    
    
    // Schedule operations
    @Override
    public Schedule getScheduleById(Long id) {
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule != null) {
            // 加载关联数据
            schedule.setType(scheduleTypeMapper.selectById(schedule.getTypeId()));
            schedule.setTags(scheduleTagMapMapper.selectTagsByScheduleId(id));
            schedule.setReminders(scheduleReminderMapper.selectByScheduleId(id));
            schedule.setAiSuggestions(scheduleAISuggestionMapper.selectByScheduleId(id));
            
        }
        return schedule;
    }
    
    @Override
    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Schedule> getSchedulesByUserIdAndDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleMapper.selectByUserIdAndDateRange(userId, startTime, endTime);
    }
    
    @Transactional
    @Override
    public Schedule createSchedule(Schedule schedule) {
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.insert(schedule);
        
        // Publish event
        publishScheduleEvent(schedule, "CREATED");
        
        return schedule;
    }
    
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.update(schedule);
        
        // Publish event
        publishScheduleEvent(schedule, "UPDATED");
        
        return schedule;
    }
    
    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        // Delete related data
        deleteRemindersByScheduleId(id);
        deleteAISuggestionsByScheduleId(id);
        
        scheduleTagMapMapper.deleteByScheduleId(id);
        
        // Delete schedule
        scheduleMapper.deleteById(id);
        
        // Publish event
        Schedule schedule = new Schedule();
        schedule.setId(id);
        publishScheduleEvent(schedule, "DELETED");
    }
    
    @Override
    public List<Schedule> getSchedulesByTypeId(Long typeId) {
        return scheduleMapper.selectByTypeId(typeId);
    }
    
    @Override
    public List<Schedule> getSchedulesByUserIdAndStatus(Long userId, String status) {
        return scheduleMapper.selectByUserIdAndStatus(userId, status);
    }
    
    @Override
    public List<Schedule> getSchedulesByUrgencyAndImportance(Long userId, Boolean isUrgent, Boolean isImportant) {
        return scheduleMapper.selectByUrgencyAndImportance(userId, isUrgent, isImportant);
    }
    
    // Schedule Type operations
    @Override
    public ScheduleType getScheduleTypeById(Long id) {
        return scheduleTypeMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleType> getScheduleTypesByUserId(Long userId) {
        return scheduleTypeMapper.selectByUserId(userId);
    }
    
    @Transactional
    @Override
    public ScheduleType createScheduleType(ScheduleType scheduleType) {
        scheduleTypeMapper.insert(scheduleType);
        return scheduleType;
    }
    
    @Transactional
    @Override
    public ScheduleType updateScheduleType(ScheduleType scheduleType) {
        scheduleTypeMapper.update(scheduleType);
        return scheduleType;
    }
    
    @Transactional
    @Override
    public void deleteScheduleType(Long id) {
        scheduleTypeMapper.deleteById(id);
    }
    
    // Schedule Tag operations
    @Override
    public ScheduleTag getScheduleTagById(Long id) {
        return scheduleTagMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleTag> getAllScheduleTags() {
        return scheduleTagMapper.selectAll();
    }
    
    @Transactional
    @Override
    public ScheduleTag createScheduleTag(ScheduleTag scheduleTag) {
        scheduleTagMapper.insert(scheduleTag);
        return scheduleTag;
    }
    
    @Transactional
    @Override
    public ScheduleTag updateScheduleTag(ScheduleTag scheduleTag) {
        scheduleTagMapper.update(scheduleTag);
        return scheduleTag;
    }
    
    @Transactional
    @Override
    public void deleteScheduleTag(Long id) {
        // 先删除关联关系
        scheduleTagMapMapper.deleteByTagId(id);
        // 再删除标签本身
        scheduleTagMapper.deleteById(id);
    }
    
    @Transactional
    @Override
    public void addTagToSchedule(Long scheduleId, Long tagId) {
        scheduleTagMapMapper.insert(scheduleId, tagId);
    }
    
    @Transactional
    @Override
    public void removeTagFromSchedule(Long scheduleId, Long tagId) {
        scheduleTagMapMapper.delete(scheduleId, tagId);
    }
    
    // Reminder operations
    @Override
    public ScheduleReminder getReminderById(Long id) {
        return scheduleReminderMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleReminder> getRemindersByScheduleId(Long scheduleId) {
        return scheduleReminderMapper.selectByScheduleId(scheduleId);
    }
    
    @Override
    public List<ScheduleReminder> getPendingReminders() {
        return scheduleReminderMapper.selectPendingReminders(LocalDateTime.now());
    }
    
    @Transactional
    @Override
    public ScheduleReminder createReminder(ScheduleReminder reminder) {
        scheduleReminderMapper.insert(reminder);
        return reminder;
    }
    
    @Transactional
    @Override
    public ScheduleReminder updateReminder(ScheduleReminder reminder) {
        scheduleReminderMapper.update(reminder);
        return reminder;
    }
    
    @Transactional
    @Override
    public void deleteReminder(Long id) {
        scheduleReminderMapper.deleteById(id);
    }
    
    @Transactional
    @Override
    public void deleteRemindersByScheduleId(Long scheduleId) {
        scheduleReminderMapper.deleteByScheduleId(scheduleId);
    }
    
    // AI Suggestion operations
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
    
    // Weather operations
    @Override
    public WeatherCache getWeatherCache(String cacheKey) {
        return weatherCacheMapper.selectById(cacheKey);
    }
    
    @Override
    public WeatherCache getValidWeatherCache(String cacheKey) {
        return weatherCacheMapper.selectValidById(cacheKey, LocalDateTime.now());
    }
    
    @Transactional
    @Override
    public WeatherCache saveWeatherCache(WeatherCache weatherCache) {
        WeatherCache existing = weatherCacheMapper.selectById(weatherCache.getCacheKey());
        if (existing != null) {
            weatherCacheMapper.update(weatherCache);
        } else {
            weatherCacheMapper.insert(weatherCache);
        }
        return weatherCache;
    }
    
    @Transactional
    @Override
    public void deleteWeatherCache(String cacheKey) {
        weatherCacheMapper.deleteById(cacheKey);
    }
    
    @Transactional
    @Override
    public void cleanExpiredWeatherCache() {
        weatherCacheMapper.deleteExpired(LocalDateTime.now());
    }
    
    // Event publishing methods
    @Override
    public void publishScheduleEvent(Schedule schedule, String eventType) {
        // In a real implementation, this would publish to a message queue
        System.out.println("Publishing schedule event: " + eventType + " for schedule ID: " + schedule.getId());
    }
    
    @Override
    public void publishWeatherEvent(String weatherInfo) {
        // In a real implementation, this would publish to a message queue
        System.out.println("Publishing weather event: " + weatherInfo);
    }
    
    @Override
    public void publishReminderEvent(ScheduleReminder reminder) {
        // In a real implementation, this would publish to a message queue
        System.out.println("Publishing reminder event for schedule ID: " + reminder.getScheduleId());
    }
}