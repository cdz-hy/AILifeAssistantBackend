package org.example.schedule.service.impl;

import org.example.schedule.entity.Schedule;
import org.example.schedule.mapper.*;
import org.example.schedule.service.ScheduleManagementService;
import org.example.schedule.util.RRuleUtils;
import org.example.schedule.util.RecurrenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日程管理服务实现类
 * 实现日程管理相关业务逻辑
 */
@Service
public class ScheduleManagementServiceImpl implements ScheduleManagementService {
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    private ScheduleTypeMapper scheduleTypeMapper;
    
    @Autowired
    private ScheduleTagMapMapper scheduleTagMapMapper;
    
    @Autowired
    private ScheduleReminderMapper scheduleReminderMapper;
    
    @Autowired
    private ScheduleAISuggestionMapper scheduleAISuggestionMapper;
    
    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleMapper.selectById(id);
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
    
    @Transactional
    @Override
    public Schedule createSchedule(Schedule schedule) {
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.insert(schedule);
        return schedule;
    }
    
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.update(schedule);
        return schedule;
    }
    
    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        scheduleMapper.deleteById(id);
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
}