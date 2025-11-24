package org.example.schedule.service;

import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.ScheduleReminder;

/**
 * 事件发布服务接口
 * 负责发布各种业务事件
 */
public interface EventPublishingService {
    
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