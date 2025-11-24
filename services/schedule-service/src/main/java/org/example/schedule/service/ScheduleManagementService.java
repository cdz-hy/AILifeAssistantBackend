package org.example.schedule.service;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程管理服务接口
 * 负责日程核心业务逻辑
 */
public interface ScheduleManagementService {
    
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
}