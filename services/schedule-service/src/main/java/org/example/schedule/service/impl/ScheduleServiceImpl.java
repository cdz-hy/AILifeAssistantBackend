package org.example.schedule.service.impl;

import org.example.schedule.entity.*;
import org.example.schedule.mapper.*;
import org.example.schedule.service.WeatherService;
import org.example.schedule.service.ScheduleService;
import org.example.schedule.service.impl.ScheduleLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
    
    @Autowired
    private ScheduleLifecycleService scheduleLifecycleService;
    
    
    
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
        // 实例保留原始的重复规则
        instance.setRecurrenceRule(original.getRecurrenceRule());
        // 同时保留RecurrencePattern对象，以便前端可以使用
        instance.setRecurrencePattern(original.getRecurrencePattern());
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
        
        // 处理带RRULE:前缀的规则
        String cleanRule = recurrenceRule;
        if (recurrenceRule.startsWith("RRULE:")) {
            cleanRule = recurrenceRule.substring(6); // 移除"RRULE:"前缀
        }
        
        if (cleanRule.contains("FREQ=DAILY")) {
            int interval = getInterval(cleanRule, 1);
            return current.plusDays(interval);
        } else if (cleanRule.contains("FREQ=WEEKLY")) {
            int interval = getInterval(cleanRule, 1);
            
            // 检查是否有BYDAY参数
            if (cleanRule.contains("BYDAY=")) {
                // 解析BYDAY参数
                DayOfWeek targetDay = getDayOfWeekFromRule(cleanRule);
                if (targetDay != null) {
                    // 计算到目标星期几的天数差
                    int daysUntilTarget = (targetDay.getValue() - current.getDayOfWeek().getValue() + 7) % 7;
                    if (daysUntilTarget == 0 && interval > 1) {
                        // 如果是同一天且间隔大于1，则增加间隔周数
                        return current.plusWeeks(interval);
                    } else if (daysUntilTarget == 0) {
                        // 如果是同一天且间隔为1，则增加一周
                        return current.plusWeeks(1);
                    } else {
                        // 返回到目标星期几的日期
                        return current.plusDays(daysUntilTarget);
                    }
                }
            }
            
            // 默认处理方式
            return current.plusWeeks(interval);
        } else if (cleanRule.contains("FREQ=MONTHLY")) {
            int interval = getInterval(cleanRule, 1);
            
            // 检查是否有BYMONTHDAY参数
            if (cleanRule.contains("BYMONTHDAY=")) {
                // 解析BYMONTHDAY参数
                List<Integer> daysOfMonth = getDaysOfMonthFromRule(cleanRule);
                if (!daysOfMonth.isEmpty()) {
                    // 查找下一个应该重复的日期
                    LocalDateTime nextDate = findNextMonthlyOccurrence(current, daysOfMonth, interval);
                    if (nextDate != null) {
                        return nextDate;
                    }
                }
            }
            
            // 默认处理方式
            return current.plusMonths(interval);
        } else if (cleanRule.contains("FREQ=YEARLY")) {
            int interval = getInterval(cleanRule, 1);
            
            // 检查是否有BYMONTH参数
            if (cleanRule.contains("BYMONTH=")) {
                // 解析BYMONTH参数
                List<Integer> monthsOfYear = getMonthsOfYearFromRule(cleanRule);
                if (!monthsOfYear.isEmpty()) {
                    // 查找下一个应该重复的月份
                    LocalDateTime nextDate = findNextYearlyOccurrence(current, monthsOfYear, interval);
                    if (nextDate != null) {
                        return nextDate;
                    }
                }
            }
            
            // 默认处理方式
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
    
    /**
     * 从重复规则中提取星期几
     * @param recurrenceRule 重复规则
     * @return 星期几
     */
    private DayOfWeek getDayOfWeekFromRule(String recurrenceRule) {
        if (recurrenceRule.contains("BYDAY=")) {
            int startIndex = recurrenceRule.indexOf("BYDAY=") + 6;
            int endIndex = recurrenceRule.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = recurrenceRule.length();
            }
            String dayStr = recurrenceRule.substring(startIndex, endIndex);
            
            switch (dayStr) {
                case "MO": return DayOfWeek.MONDAY;
                case "TU": return DayOfWeek.TUESDAY;
                case "WE": return DayOfWeek.WEDNESDAY;
                case "TH": return DayOfWeek.THURSDAY;
                case "FR": return DayOfWeek.FRIDAY;
                case "SA": return DayOfWeek.SATURDAY;
                case "SU": return DayOfWeek.SUNDAY;
                default: return null;
            }
        }
        return null;
    }
    
    /**
     * 从重复规则中提取月中日期
     * @param recurrenceRule 重复规则
     * @return 月中日期列表
     */
    private List<Integer> getDaysOfMonthFromRule(String recurrenceRule) {
        List<Integer> days = new ArrayList<>();
        if (recurrenceRule.contains("BYMONTHDAY=")) {
            int startIndex = recurrenceRule.indexOf("BYMONTHDAY=") + 11;
            int endIndex = recurrenceRule.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = recurrenceRule.length();
            }
            String daysStr = recurrenceRule.substring(startIndex, endIndex);
            String[] dayArray = daysStr.split(",");
            
            for (String dayStr : dayArray) {
                try {
                    int day = Integer.parseInt(dayStr);
                    // 确保日期在有效范围内
                    if (day >= 1 && day <= 31) {
                        days.add(day);
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的数字
                }
            }
        }
        return days;
    }
    
    /**
     * 查找下一个月度重复日期
     * @param current 当前日期
     * @param daysOfMonth 月中日期列表
     * @param interval 间隔月数
     * @return 下一个重复日期
     */
    private LocalDateTime findNextMonthlyOccurrence(LocalDateTime current, List<Integer> daysOfMonth, int interval) {
        // 对日期进行排序
        List<Integer> sortedDays = new ArrayList<>(daysOfMonth);
        Collections.sort(sortedDays);
        
        // 查找当前月内下一个可能的日期
        for (Integer day : sortedDays) {
            if (day >= current.getDayOfMonth()) {
                // 检查日期是否有效（例如，不是2月30日）
                if (isValidDayInMonth(current.getYear(), current.getMonthValue(), day)) {
                    LocalDateTime candidate = current.withDayOfMonth(day);
                    if (candidate.isAfter(current)) {
                        return candidate;
                    }
                }
            }
        }
        
        // 如果当前月内没有合适的日期，则转到下一月
        LocalDateTime nextMonth = current.plusMonths(interval);
        // 在下一月中查找最早的日期
        for (Integer day : sortedDays) {
            if (isValidDayInMonth(nextMonth.getYear(), nextMonth.getMonthValue(), day)) {
                return nextMonth.withDayOfMonth(day);
            }
        }
        
        return null;
    }
    
    /**
     * 检查指定年月的日期是否有效
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日期是否有效
     */
    private boolean isValidDayInMonth(int year, int month, int day) {
        if (day < 1 || day > 31) return false;
        
        // 每月的天数
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        // 闰年2月有29天
        if (month == 2 && isLeapYear(year)) {
            return day <= 29;
        }
        
        // 检查是否超过该月的最大天数
        if (month >= 1 && month <= 12) {
            return day <= daysInMonth[month - 1];
        }
        
        return false;
    }
    
    /**
     * 判断是否为闰年
     * @param year 年
     * @return 是否为闰年
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    /**
     * 从重复规则中提取年中月份
     * @param recurrenceRule 重复规则
     * @return 年中月份列表
     */
    private List<Integer> getMonthsOfYearFromRule(String recurrenceRule) {
        List<Integer> months = new ArrayList<>();
        if (recurrenceRule.contains("BYMONTH=")) {
            int startIndex = recurrenceRule.indexOf("BYMONTH=") + 8;
            int endIndex = recurrenceRule.indexOf(";", startIndex);
            if (endIndex == -1) {
                endIndex = recurrenceRule.length();
            }
            String monthsStr = recurrenceRule.substring(startIndex, endIndex);
            String[] monthArray = monthsStr.split(",");
            
            for (String monthStr : monthArray) {
                try {
                    int month = Integer.parseInt(monthStr);
                    // 确保月份在有效范围内
                    if (month >= 1 && month <= 12) {
                        months.add(month);
                    }
                } catch (NumberFormatException e) {
                    // 忽略无效的数字
                }
            }
        }
        return months;
    }
    
    /**
     * 查找下一个年度重复日期
     * @param current 当前日期
     * @param monthsOfYear 年中月份列表
     * @param interval 间隔年数
     * @return 下一个重复日期
     */
    private LocalDateTime findNextYearlyOccurrence(LocalDateTime current, List<Integer> monthsOfYear, int interval) {
        // 对月份进行排序
        List<Integer> sortedMonths = new ArrayList<>(monthsOfYear);
        Collections.sort(sortedMonths);
        
        // 获取当前日期的日期部分（几号）
        int targetDay = current.getDayOfMonth();
        
        // 查找当前年内下一个可能的月份
        for (Integer month : sortedMonths) {
            if (month >= current.getMonthValue()) {
                // 尝试在目标月份设置相同的日期
                if (isValidDayInMonth(current.getYear(), month, targetDay)) {
                    LocalDateTime candidate = current.withMonth(month).withDayOfMonth(targetDay);
                    if (candidate.isAfter(current)) {
                        return candidate;
                    }
                }
                // 如果当前月且日期不匹配，检查下一个月份
                else if (month > current.getMonthValue()) {
                    // 尝试在目标月份设置相同的日期
                    if (isValidDayInMonth(current.getYear(), month, targetDay)) {
                        return current.withMonth(month).withDayOfMonth(targetDay);
                    }
                }
            }
        }
        
        // 如果当前年内没有合适的月份，则转到下一年
        LocalDateTime nextYear = current.plusYears(interval);
        // 在下一年中查找最早的月份
        for (Integer month : sortedMonths) {
            if (isValidDayInMonth(nextYear.getYear(), month, targetDay)) {
                return nextYear.withMonth(month).withDayOfMonth(targetDay);
            }
        }
        
        return null;
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
        
        // 创建日程生命周期任务
        scheduleLifecycleService.createOrUpdateScheduleLifecycle(schedule);
        
        return schedule;
    }
    
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.update(schedule);
        
        // Publish event
        publishScheduleEvent(schedule, "UPDATED");
        
        // 更新日程生命周期任务
        scheduleLifecycleService.createOrUpdateScheduleLifecycle(schedule);
        
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