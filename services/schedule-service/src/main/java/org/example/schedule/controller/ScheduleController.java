package org.example.schedule.controller;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.ScheduleType;
import org.example.schedule.entity.ScheduleTag;
import org.example.schedule.entity.ScheduleReminder;
import org.example.schedule.entity.ScheduleAISuggestion;
import org.example.schedule.entity.ScheduleException;
import org.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程控制器
 * 处理日程相关的HTTP请求
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    // Schedule endpoints
    /**
     * 根据ID获取日程
     * @param id 日程ID
     * @return 日程实体
     */
    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }
    
    /**
     * 根据用户ID获取所有日程
     * @param userId 用户ID
     * @return 日程列表
     */
    @GetMapping("/user/{userId}")
    public List<Schedule> getSchedulesByUserId(@PathVariable Long userId) {
        return scheduleService.getSchedulesByUserId(userId);
    }
    
    /**
     * 根据用户ID和日期范围获取日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    @GetMapping("/user/{userId}/range")
    public List<Schedule> getSchedulesByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return scheduleService.getSchedulesByUserIdAndDateRange(userId, start, end);
    }
    
    /**
     * 根据类型ID获取日程
     * @param typeId 类型ID
     * @return 日程列表
     */
    @GetMapping("/type/{typeId}")
    public List<Schedule> getSchedulesByTypeId(@PathVariable Long typeId) {
        return scheduleService.getSchedulesByTypeId(typeId);
    }
    
    /**
     * 根据状态获取日程
     * @param userId 用户ID
     * @param status 状态
     * @return 日程列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public List<Schedule> getSchedulesByUserIdAndStatus(@PathVariable Long userId, @PathVariable String status) {
        return scheduleService.getSchedulesByUserIdAndStatus(userId, status);
    }
    
    /**
     * 根据紧急重要性获取日程
     * @param userId 用户ID
     * @param isUrgent 是否紧急
     * @param isImportant 是否重要
     * @return 日程列表
     */
    @GetMapping("/user/{userId}/urgency")
    public List<Schedule> getSchedulesByUrgencyAndImportance(
            @PathVariable Long userId,
            @RequestParam Boolean isUrgent,
            @RequestParam Boolean isImportant) {
        return scheduleService.getSchedulesByUrgencyAndImportance(userId, isUrgent, isImportant);
    }
    
    /**
     * 创建日程
     * @param schedule 日程实体
     * @return 创建后的日程实体
     */
    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }
    
    /**
     * 更新日程
     * @param id 日程ID
     * @param schedule 日程实体
     * @return 更新后的日程实体
     */
    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        return scheduleService.updateSchedule(schedule);
    }
    
    /**
     * 删除日程
     * @param id 日程ID
     */
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }
    
    // Schedule Type endpoints
    /**
     * 根据ID获取日程类型
     * @param id 类型ID
     * @return 日程类型实体
     */
    @GetMapping("/types/{id}")
    public ScheduleType getScheduleTypeById(@PathVariable Long id) {
        return scheduleService.getScheduleTypeById(id);
    }
    
    /**
     * 根据用户ID获取日程类型
     * @param userId 用户ID
     * @return 日程类型列表
     */
    @GetMapping("/types/user/{userId}")
    public List<ScheduleType> getScheduleTypesByUserId(@PathVariable Long userId) {
        return scheduleService.getScheduleTypesByUserId(userId);
    }
    
    /**
     * 创建日程类型
     * @param scheduleType 日程类型实体
     * @return 创建后的日程类型实体
     */
    @PostMapping("/types")
    public ScheduleType createScheduleType(@RequestBody ScheduleType scheduleType) {
        return scheduleService.createScheduleType(scheduleType);
    }
    
    /**
     * 更新日程类型
     * @param id 类型ID
     * @param scheduleType 日程类型实体
     * @return 更新后的日程类型实体
     */
    @PutMapping("/types/{id}")
    public ScheduleType updateScheduleType(@PathVariable Long id, @RequestBody ScheduleType scheduleType) {
        scheduleType.setId(id);
        return scheduleService.updateScheduleType(scheduleType);
    }
    
    /**
     * 删除日程类型
     * @param id 类型ID
     */
    @DeleteMapping("/types/{id}")
    public void deleteScheduleType(@PathVariable Long id) {
        scheduleService.deleteScheduleType(id);
    }
    
    // Schedule Tag endpoints
    /**
     * 根据ID获取日程标签
     * @param id 标签ID
     * @return 日程标签实体
     */
    @GetMapping("/tags/{id}")
    public ScheduleTag getScheduleTagById(@PathVariable Long id) {
        return scheduleService.getScheduleTagById(id);
    }
    
    /**
     * 获取所有日程标签
     * @return 日程标签列表
     */
    @GetMapping("/tags")
    public List<ScheduleTag> getAllScheduleTags() {
        return scheduleService.getAllScheduleTags();
    }
    
    /**
     * 创建日程标签
     * @param scheduleTag 日程标签实体
     * @return 创建后的日程标签实体
     */
    @PostMapping("/tags")
    public ScheduleTag createScheduleTag(@RequestBody ScheduleTag scheduleTag) {
        return scheduleService.createScheduleTag(scheduleTag);
    }
    
    /**
     * 更新日程标签
     * @param id 标签ID
     * @param scheduleTag 日程标签实体
     * @return 更新后的日程标签实体
     */
    @PutMapping("/tags/{id}")
    public ScheduleTag updateScheduleTag(@PathVariable Long id, @RequestBody ScheduleTag scheduleTag) {
        scheduleTag.setId(id);
        return scheduleService.updateScheduleTag(scheduleTag);
    }
    
    /**
     * 删除日程标签
     * @param id 标签ID
     */
    @DeleteMapping("/tags/{id}")
    public void deleteScheduleTag(@PathVariable Long id) {
        scheduleService.deleteScheduleTag(id);
    }
    
    /**
     * 为日程添加标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    @PostMapping("/{scheduleId}/tags/{tagId}")
    public void addTagToSchedule(@PathVariable Long scheduleId, @PathVariable Long tagId) {
        scheduleService.addTagToSchedule(scheduleId, tagId);
    }
    
    /**
     * 移除日程的标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    @DeleteMapping("/{scheduleId}/tags/{tagId}")
    public void removeTagFromSchedule(@PathVariable Long scheduleId, @PathVariable Long tagId) {
        scheduleService.removeTagFromSchedule(scheduleId, tagId);
    }
    
    // Reminder endpoints
    /**
     * 根据ID获取提醒
     * @param id 提醒ID
     * @return 提醒实体
     */
    @GetMapping("/reminders/{id}")
    public ScheduleReminder getReminderById(@PathVariable Long id) {
        return scheduleService.getReminderById(id);
    }
    
    /**
     * 根据日程ID获取提醒
     * @param scheduleId 日程ID
     * @return 提醒列表
     */
    @GetMapping("/reminders/schedule/{scheduleId}")
    public List<ScheduleReminder> getRemindersByScheduleId(@PathVariable Long scheduleId) {
        return scheduleService.getRemindersByScheduleId(scheduleId);
    }
    
    /**
     * 获取待处理的提醒
     * @return 提醒列表
     */
    @GetMapping("/reminders/pending")
    public List<ScheduleReminder> getPendingReminders() {
        return scheduleService.getPendingReminders();
    }
    
    /**
     * 创建提醒
     * @param reminder 提醒实体
     * @return 创建后的提醒实体
     */
    @PostMapping("/reminders")
    public ScheduleReminder createReminder(@RequestBody ScheduleReminder reminder) {
        return scheduleService.createReminder(reminder);
    }
    
    /**
     * 更新提醒
     * @param id 提醒ID
     * @param reminder 提醒实体
     * @return 更新后的提醒实体
     */
    @PutMapping("/reminders/{id}")
    public ScheduleReminder updateReminder(@PathVariable Long id, @RequestBody ScheduleReminder reminder) {
        reminder.setId(id);
        return scheduleService.updateReminder(reminder);
    }
    
    /**
     * 删除提醒
     * @param id 提醒ID
     */
    @DeleteMapping("/reminders/{id}")
    public void deleteReminder(@PathVariable Long id) {
        scheduleService.deleteReminder(id);
    }
    
    // AI Suggestion endpoints
    /**
     * 根据ID获取AI建议
     * @param id 建议ID
     * @return AI建议实体
     */
    @GetMapping("/suggestions/{id}")
    public ScheduleAISuggestion getAISuggestionById(@PathVariable Long id) {
        return scheduleService.getAISuggestionById(id);
    }
    
    /**
     * 根据日程ID获取AI建议
     * @param scheduleId 日程ID
     * @return AI建议列表
     */
    @GetMapping("/suggestions/schedule/{scheduleId}")
    public List<ScheduleAISuggestion> getAISuggestionsByScheduleId(@PathVariable Long scheduleId) {
        return scheduleService.getAISuggestionsByScheduleId(scheduleId);
    }
    
    /**
     * 创建AI建议
     * @param aiSuggestion AI建议实体
     * @return 创建后的AI建议实体
     */
    @PostMapping("/suggestions")
    public ScheduleAISuggestion createAISuggestion(@RequestBody ScheduleAISuggestion aiSuggestion) {
        return scheduleService.createAISuggestion(aiSuggestion);
    }
    
    /**
     * 更新AI建议
     * @param id 建议ID
     * @param aiSuggestion AI建议实体
     * @return 更新后的AI建议实体
     */
    @PutMapping("/suggestions/{id}")
    public ScheduleAISuggestion updateAISuggestion(@PathVariable Long id, @RequestBody ScheduleAISuggestion aiSuggestion) {
        aiSuggestion.setId(id);
        return scheduleService.updateAISuggestion(aiSuggestion);
    }
    
    /**
     * 删除AI建议
     * @param id 建议ID
     */
    @DeleteMapping("/suggestions/{id}")
    public void deleteAISuggestion(@PathVariable Long id) {
        scheduleService.deleteAISuggestion(id);
    }
    
    // Schedule Exception endpoints
    /**
     * 根据ID获取日程例外
     * @param id 例外ID
     * @return 日程例外实体
     */
    @GetMapping("/exceptions/{id}")
    public ScheduleException getScheduleExceptionById(@PathVariable Long id) {
        return scheduleService.getScheduleExceptionById(id);
    }
    
    /**
     * 根据日程ID获取例外
     * @param scheduleId 日程ID
     * @return 日程例外列表
     */
    @GetMapping("/exceptions/schedule/{scheduleId}")
    public List<ScheduleException> getScheduleExceptionsByScheduleId(@PathVariable Long scheduleId) {
        return scheduleService.getScheduleExceptionsByScheduleId(scheduleId);
    }
    
    /**
     * 创建日程例外
     * @param scheduleException 日程例外实体
     * @return 创建后的日程例外实体
     */
    @PostMapping("/exceptions")
    public ScheduleException createScheduleException(@RequestBody ScheduleException scheduleException) {
        return scheduleService.createScheduleException(scheduleException);
    }
    
    /**
     * 更新日程例外
     * @param id 例外ID
     * @param scheduleException 日程例外实体
     * @return 更新后的日程例外实体
     */
    @PutMapping("/exceptions/{id}")
    public ScheduleException updateScheduleException(@PathVariable Long id, @RequestBody ScheduleException scheduleException) {
        scheduleException.setId(id);
        return scheduleService.updateScheduleException(scheduleException);
    }
    
    /**
     * 删除日程例外
     * @param id 例外ID
     */
    @DeleteMapping("/exceptions/{id}")
    public void deleteScheduleException(@PathVariable Long id) {
        scheduleService.deleteScheduleException(id);
    }
}