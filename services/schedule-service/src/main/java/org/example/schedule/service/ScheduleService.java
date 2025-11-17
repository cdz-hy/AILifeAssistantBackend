package org.example.schedule.service;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.ScheduleType;
import org.example.schedule.entity.ScheduleTag;
import org.example.schedule.entity.ScheduleReminder;
import org.example.schedule.entity.ScheduleAISuggestion;
import org.example.schedule.entity.WeatherCache;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程服务接口
 * 定义日程管理相关业务逻辑
 */
public interface ScheduleService {
    
    // Schedule operations
    /**
     * 根据ID获取日程
     * @param id 日程ID
     * @return 日程实体
     */
    Schedule getScheduleById(Long id);
    
    /**
     * 根据用户ID获取所有日程
     * @param userId 用户ID
     * @return 日程列表
     */
    List<Schedule> getSchedulesByUserId(Long userId);
    
    /**
     * 根据用户ID和日期范围获取日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    List<Schedule> getSchedulesByUserIdAndDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 创建日程
     * @param schedule 日程实体
     * @return 创建后的日程实体
     */
    Schedule createSchedule(Schedule schedule);
    
    /**
     * 更新日程
     * @param schedule 日程实体
     * @return 更新后的日程实体
     */
    Schedule updateSchedule(Schedule schedule);
    
    /**
     * 删除日程
     * @param id 日程ID
     */
    void deleteSchedule(Long id);
    
    /**
     * 根据类型ID获取日程
     * @param typeId 类型ID
     * @return 日程列表
     */
    List<Schedule> getSchedulesByTypeId(Long typeId);
    
    /**
     * 根据状态获取日程
     * @param userId 用户ID
     * @param status 状态
     * @return 日程列表
     */
    List<Schedule> getSchedulesByUserIdAndStatus(Long userId, String status);
    
    /**
     * 根据紧急重要性获取日程
     * @param userId 用户ID
     * @param isUrgent 是否紧急
     * @param isImportant 是否重要
     * @return 日程列表
     */
    List<Schedule> getSchedulesByUrgencyAndImportance(Long userId, Boolean isUrgent, Boolean isImportant);
    
    // Schedule Type operations
    /**
     * 根据ID获取日程类型
     * @param id 类型ID
     * @return 日程类型实体
     */
    ScheduleType getScheduleTypeById(Long id);
    
    /**
     * 根据用户ID获取日程类型
     * @param userId 用户ID
     * @return 日程类型列表
     */
    List<ScheduleType> getScheduleTypesByUserId(Long userId);
    
    /**
     * 创建日程类型
     * @param scheduleType 日程类型实体
     * @return 创建后的日程类型实体
     */
    ScheduleType createScheduleType(ScheduleType scheduleType);
    
    /**
     * 更新日程类型
     * @param scheduleType 日程类型实体
     * @return 更新后的日程类型实体
     */
    ScheduleType updateScheduleType(ScheduleType scheduleType);
    
    /**
     * 删除日程类型
     * @param id 类型ID
     */
    void deleteScheduleType(Long id);
    
    // Schedule Tag operations
    /**
     * 根据ID获取日程标签
     * @param id 标签ID
     * @return 日程标签实体
     */
    ScheduleTag getScheduleTagById(Long id);
    
    /**
     * 获取所有日程标签
     * @return 日程标签列表
     */
    List<ScheduleTag> getAllScheduleTags();
    
    /**
     * 创建日程标签
     * @param scheduleTag 日程标签实体
     * @return 创建后的日程标签实体
     */
    ScheduleTag createScheduleTag(ScheduleTag scheduleTag);
    
    /**
     * 更新日程标签
     * @param scheduleTag 日程标签实体
     * @return 更新后的日程标签实体
     */
    ScheduleTag updateScheduleTag(ScheduleTag scheduleTag);
    
    /**
     * 删除日程标签
     * @param id 标签ID
     */
    void deleteScheduleTag(Long id);
    
    /**
     * 为日程添加标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    void addTagToSchedule(Long scheduleId, Long tagId);
    
    /**
     * 移除日程的标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    void removeTagFromSchedule(Long scheduleId, Long tagId);
    
    // Reminder operations
    /**
     * 根据ID获取提醒
     * @param id 提醒ID
     * @return 提醒实体
     */
    ScheduleReminder getReminderById(Long id);
    
    /**
     * 根据日程ID获取提醒
     * @param scheduleId 日程ID
     * @return 提醒列表
     */
    List<ScheduleReminder> getRemindersByScheduleId(Long scheduleId);
    
    /**
     * 获取待处理的提醒
     * @return 提醒列表
     */
    List<ScheduleReminder> getPendingReminders();
    
    /**
     * 创建提醒
     * @param reminder 提醒实体
     * @return 创建后的提醒实体
     */
    ScheduleReminder createReminder(ScheduleReminder reminder);
    
    /**
     * 更新提醒
     * @param reminder 提醒实体
     * @return 更新后的提醒实体
     */
    ScheduleReminder updateReminder(ScheduleReminder reminder);
    
    /**
     * 删除提醒
     * @param id 提醒ID
     */
    void deleteReminder(Long id);
    
    /**
     * 根据日程ID删除提醒
     * @param scheduleId 日程ID
     */
    void deleteRemindersByScheduleId(Long scheduleId);
    
    // AI Suggestion operations
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
    
    // Weather operations
    /**
     * 根据缓存键获取天气缓存
     * @param cacheKey 缓存键
     * @return 天气缓存实体
     */
    WeatherCache getWeatherCache(String cacheKey);
    
    /**
     * 根据缓存键获取有效的天气缓存
     * @param cacheKey 缓存键
     * @return 天气缓存实体
     */
    WeatherCache getValidWeatherCache(String cacheKey);
    
    /**
     * 保存天气缓存
     * @param weatherCache 天气缓存实体
     * @return 保存后的天气缓存实体
     */
    WeatherCache saveWeatherCache(WeatherCache weatherCache);
    
    /**
     * 删除天气缓存
     * @param cacheKey 缓存键
     */
    void deleteWeatherCache(String cacheKey);
    
    /**
     * 清理过期的天气缓存
     */
    void cleanExpiredWeatherCache();
    
    // Event publishing methods
    /**
     * 发布日程事件
     * @param schedule 日程实体
     * @param eventType 事件类型
     */
    void publishScheduleEvent(Schedule schedule, String eventType);
    
    /**
     * 发布天气事件
     * @param weatherInfo 天气信息
     */
    void publishWeatherEvent(String weatherInfo);
    
    /**
     * 发布提醒事件
     * @param reminder 提醒实体
     */
    void publishReminderEvent(ScheduleReminder reminder);
}