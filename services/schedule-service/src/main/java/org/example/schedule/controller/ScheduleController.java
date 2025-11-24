package org.example.schedule.controller;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.ScheduleType;
import org.example.schedule.entity.ScheduleTag;
import org.example.schedule.service.ScheduleManagementService;
import org.example.schedule.service.ScheduleTypeService;
import org.example.schedule.service.ScheduleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 日程控制器
 * 处理日程相关的HTTP请求
 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    
    @Autowired
    private ScheduleManagementService scheduleManagementService;
    
    @Autowired
    private ScheduleTypeService scheduleTypeService;
    
    @Autowired
    private ScheduleTagService scheduleTagService;
    
    // Schedule endpoints
    /**
     * 根据ID获取日程
     * @param id 日程ID
     * @return 日程实体
     */
    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Long id) {
        return scheduleManagementService.getScheduleById(id);
    }
    
    /**
     * 根据用户ID获取所有日程
     * @param userId 用户ID
     * @return 日程列表
     */
    @GetMapping("/user/{userId}")
    public List<Schedule> getSchedulesByUserId(@PathVariable Long userId) {
        return scheduleManagementService.getSchedulesByUserId(userId);
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
        // 使用Instant处理带时区的ISO 8601格式时间
        Instant startInstant = Instant.parse(startTime);
        Instant endInstant = Instant.parse(endTime);
        
        // 转换为LocalDateTime（忽略时区信息）
        LocalDateTime start = startInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        return scheduleManagementService.getSchedulesByUserIdAndDateRange(userId, start, end);
    }
    
    /**
     * 根据类型ID获取日程
     * @param typeId 类型ID
     * @return 日程列表
     */
    @GetMapping("/type/{typeId}")
    public List<Schedule> getSchedulesByTypeId(@PathVariable Long typeId) {
        return scheduleManagementService.getSchedulesByTypeId(typeId);
    }
    
    /**
     * 根据状态获取日程
     * @param userId 用户ID
     * @param status 状态
     * @return 日程列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public List<Schedule> getSchedulesByUserIdAndStatus(@PathVariable Long userId, @PathVariable String status) {
        return scheduleManagementService.getSchedulesByUserIdAndStatus(userId, status);
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
        return scheduleManagementService.getSchedulesByUrgencyAndImportance(userId, isUrgent, isImportant);
    }
    
    /**
     * 创建日程
     * @param schedule 日程实体
     * @return 创建后的日程实体
     */
    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleManagementService.createSchedule(schedule);
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
        return scheduleManagementService.updateSchedule(schedule);
    }
    
    /**
     * 删除日程
     * @param id 日程ID
     */
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleManagementService.deleteSchedule(id);
    }
    
    // Schedule Type endpoints
    /**
     * 根据ID获取日程类型
     * @param id 类型ID
     * @return 日程类型实体
     */
    @GetMapping("/types/{id}")
    public ScheduleType getScheduleTypeById(@PathVariable Long id) {
        return scheduleTypeService.getScheduleTypeById(id);
    }
    
    /**
     * 根据用户ID获取日程类型
     * @param userId 用户ID
     * @return 日程类型列表
     */
    @GetMapping("/types/user/{userId}")
    public List<ScheduleType> getScheduleTypesByUserId(@PathVariable Long userId) {
        return scheduleTypeService.getScheduleTypesByUserId(userId);
    }
    
    /**
     * 创建日程类型
     * @param scheduleType 日程类型实体
     * @return 创建后的日程类型实体
     */
    @PostMapping("/types")
    public ScheduleType createScheduleType(@RequestBody ScheduleType scheduleType) {
        return scheduleTypeService.createScheduleType(scheduleType);
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
        return scheduleTypeService.updateScheduleType(scheduleType);
    }
    
    /**
     * 删除日程类型
     * @param id 类型ID
     */
    @DeleteMapping("/types/{id}")
    public void deleteScheduleType(@PathVariable Long id) {
        scheduleTypeService.deleteScheduleType(id);
    }
    
    // Schedule Tag endpoints
    /**
     * 根据ID获取日程标签
     * @param id 标签ID
     * @return 日程标签实体
     */
    @GetMapping("/tags/{id}")
    public ScheduleTag getScheduleTagById(@PathVariable Long id) {
        return scheduleTagService.getScheduleTagById(id);
    }
    
    /**
     * 获取所有日程标签
     * @return 日程标签列表
     */
    @GetMapping("/tags")
    public List<ScheduleTag> getAllScheduleTags() {
        return scheduleTagService.getAllScheduleTags();
    }
    
    /**
     * 创建日程标签
     * @param scheduleTag 日程标签实体
     * @return 创建后的日程标签实体
     */
    @PostMapping("/tags")
    public ScheduleTag createScheduleTag(@RequestBody ScheduleTag scheduleTag) {
        return scheduleTagService.createScheduleTag(scheduleTag);
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
        return scheduleTagService.updateScheduleTag(scheduleTag);
    }
    
    /**
     * 删除日程标签
     * @param id 标签ID
     */
    @DeleteMapping("/tags/{id}")
    public void deleteScheduleTag(@PathVariable Long id) {
        scheduleTagService.deleteScheduleTag(id);
    }
    
    /**
     * 为日程添加标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    @PostMapping("/{scheduleId}/tags/{tagId}")
    public void addTagToSchedule(@PathVariable Long scheduleId, @PathVariable Long tagId) {
        scheduleTagService.addTagToSchedule(scheduleId, tagId);
    }
    
    /**
     * 移除日程的标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    @DeleteMapping("/{scheduleId}/tags/{tagId}")
    public void removeTagFromSchedule(@PathVariable Long scheduleId, @PathVariable Long tagId) {
        scheduleTagService.removeTagFromSchedule(scheduleId, tagId);
    }
}