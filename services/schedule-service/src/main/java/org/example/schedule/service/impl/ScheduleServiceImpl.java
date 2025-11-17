package org.example.schedule.service.impl;

import org.example.schedule.entity.*;
import org.example.schedule.mapper.*;
import org.example.schedule.service.WeatherService;
import org.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.Duration;

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
    
    @Autowired
    private WeatherService weatherService;
    
    
    
    // Schedule operations
    
    /**
     * 生成重复日程在指定时间范围内的实例
     * @param schedule 原始重复日程
     * @param rangeStart 时间范围开始
     * @param rangeEnd 时间范围结束
     * @return 在时间范围内的日程实例列表
     */
    private List<Schedule> generateRecurringInstances(Schedule schedule, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<Schedule> instances = new ArrayList<>();
        
        // 获取重复规则
        String recurrenceRule = schedule.getRecurrenceRule();
        if (recurrenceRule == null || recurrenceRule.isEmpty()) {
            // 如果没有重复规则，只添加原始日程（如果在时间范围内）
            if (!schedule.getStartTime().isBefore(rangeStart) && !schedule.getStartTime().isAfter(rangeEnd)) {
                instances.add(schedule);
            }
            return instances;
        }
        
        // 计算时间跨度（天数）
        long daysBetween = Duration.between(rangeStart, rangeEnd).toDays();
        if (daysBetween <= 0) {
            return instances;
        }
        
        // 根据重复规则生成实例
        LocalDateTime current = schedule.getStartTime();
        int count = 0;
        // 增加最大实例数，确保能覆盖查询范围内的所有可能实例
        int maxCount = Math.max(1000, (int) daysBetween * 10); // 防止无限循环，设置最大实例数
        
        // 首先快进到查询范围的开始时间附近
        while (current.isBefore(rangeStart) && count < maxCount) {
            current = getNextOccurrence(current, recurrenceRule);
            if (current == null) {
                break;
            }
            count++;
        }
        
        // 然后生成查询范围内的实例
        while (!current.isAfter(rangeEnd) && count < maxCount) {
            // 创建日程实例
            Schedule instance = createScheduleInstance(schedule, current);
            instances.add(instance);
            
            // 计算下一个实例的时间
            current = getNextOccurrence(current, recurrenceRule);
            if (current == null) {
                break;
            }
            
            count++;
        }
        
        return instances;
    }
    
    /**
     * 根据原始日程创建实例
     * @param original 原始日程
     * @param instanceTime 实例时间
     * @return 日程实例
     */
    private Schedule createScheduleInstance(Schedule original, LocalDateTime instanceTime) {
        Schedule instance = new Schedule();
        instance.setId(original.getId());
        instance.setUserId(original.getUserId());
        instance.setTitle(original.getTitle());
        instance.setDescription(original.getDescription());
        
        // 计算持续时间
        Duration duration = Duration.between(original.getStartTime(), original.getEndTime());
        instance.setStartTime(instanceTime);
        instance.setEndTime(instanceTime.plus(duration));
        
        instance.setTypeId(original.getTypeId());
        instance.setUrgent(original.getUrgent());
        instance.setImportant(original.getImportant());
        // 实例保留原始的重复规则，但不使用RecurrencePattern对象
        instance.setRecurrenceRule(original.getRecurrenceRule());
        instance.setRecurrencePattern(null);
        instance.setStatus(original.getStatus());
        instance.setCreatedAt(original.getCreatedAt());
        instance.setUpdatedAt(original.getUpdatedAt());
        
        // 复制关联属性
        instance.setType(original.getType());
        instance.setTags(original.getTags());
        instance.setReminders(original.getReminders());
        instance.setAiSuggestions(original.getAiSuggestions());
        
        return instance;
    }
    
    /**
     * 根据重复规则计算下一次发生的时间
     * @param current 当前时间
     * @param recurrenceRule 重复规则
     * @return 下一次发生的时间，如果无法计算则返回null
     */
    private LocalDateTime getNextOccurrence(LocalDateTime current, String recurrenceRule) {
        // 这里使用简化的方法计算下一次发生的时间
        // 实际项目中可以使用iCal4j等库来处理复杂的RRULE
        
        if (recurrenceRule.contains("FREQ=DAILY")) {
            int interval = getInterval(recurrenceRule, 1);
            return current.plusDays(interval);
        } else if (recurrenceRule.contains("FREQ=WEEKLY")) {
            int interval = getInterval(recurrenceRule, 1);
            return current.plusWeeks(interval);
        } else if (recurrenceRule.contains("FREQ=MONTHLY")) {
            int interval = getInterval(recurrenceRule, 1);
            return current.plusMonths(interval);
        } else if (recurrenceRule.contains("FREQ=YEARLY")) {
            int interval = getInterval(recurrenceRule, 1);
            return current.plusYears(interval);
        }
        
        // 默认情况，无法解析规则
        return null;
    }
    
    /**
     * 从重复规则中提取间隔
     * @param recurrenceRule 重复规则
     * @param defaultInterval 默认间隔
     * @return 间隔值
     */
    private int getInterval(String recurrenceRule, int defaultInterval) {
        if (recurrenceRule.contains("INTERVAL=")) {
            int startIndex = recurrenceRule.indexOf("INTERVAL=") + 9;
            int endIndex = recurrenceRule.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = recurrenceRule.length();
            }
            try {
                return Integer.parseInt(recurrenceRule.substring(startIndex, endIndex));
            } catch (NumberFormatException e) {
                return defaultInterval;
            }
        }
        return defaultInterval;
    }
    
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
        // 查询非重复日程
        List<Schedule> nonRecurringSchedules = scheduleMapper.selectNonRecurringByUserIdAndDateRange(userId, startTime, endTime);
        
        // 查询重复日程（在结束时间之前开始的）
        List<Schedule> recurringSchedules = scheduleMapper.selectRecurringStartedBefore(userId, endTime);
        
        // 处理重复日程，生成在时间范围内的实例
        List<Schedule> recurringInstances = new ArrayList<>();
        for (Schedule schedule : recurringSchedules) {
            // 生成重复日程的实例
            recurringInstances.addAll(generateRecurringInstances(schedule, startTime, endTime));
        }
        
        // 合并结果并按开始时间排序
        List<Schedule> result = new ArrayList<>();
        result.addAll(nonRecurringSchedules);
        result.addAll(recurringInstances);
        
        // 为每个日程加载关联数据
        for (Schedule schedule : result) {
            if (schedule.getId() != null) {
                // 加载关联数据
                schedule.setType(scheduleTypeMapper.selectById(schedule.getTypeId()));
                schedule.setTags(scheduleTagMapMapper.selectTagsByScheduleId(schedule.getId()));
                schedule.setReminders(scheduleReminderMapper.selectByScheduleId(schedule.getId()));
                schedule.setAiSuggestions(scheduleAISuggestionMapper.selectByScheduleId(schedule.getId()));
            }
        }
        
        // 按开始时间排序
        return result.stream()
                .sorted((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()))
                .collect(Collectors.toList());
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