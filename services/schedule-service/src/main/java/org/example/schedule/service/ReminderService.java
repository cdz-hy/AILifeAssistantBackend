package org.example.schedule.service;

import org.example.schedule.entity.ScheduleReminder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒服务接口
 * 负责日程提醒相关业务逻辑
 */
public interface ReminderService {
    
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
}