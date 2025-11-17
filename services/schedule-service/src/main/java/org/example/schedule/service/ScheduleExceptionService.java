package org.example.schedule.service;

import org.example.schedule.entity.ScheduleException;

import java.util.List;

/**
 * 日程例外服务接口
 * 负责日程例外相关业务逻辑
 */
public interface ScheduleExceptionService {
    
    /**
     * 根据ID获取日程例外
     * @param id 例外ID
     * @return 日程例外实体
     */
    ScheduleException getScheduleExceptionById(Long id);
    
    /**
     * 根据日程ID获取例外
     * @param scheduleId 日程ID
     * @return 日程例外列表
     */
    List<ScheduleException> getScheduleExceptionsByScheduleId(Long scheduleId);
    
    /**
     * 创建日程例外
     * @param scheduleException 日程例外实体
     * @return 创建后的日程例外实体
     */
    ScheduleException createScheduleException(ScheduleException scheduleException);
    
    /**
     * 更新日程例外
     * @param scheduleException 日程例外实体
     * @return 更新后的日程例外实体
     */
    ScheduleException updateScheduleException(ScheduleException scheduleException);
    
    /**
     * 删除日程例外
     * @param id 例外ID
     */
    void deleteScheduleException(Long id);
    
    /**
     * 根据日程ID删除例外
     * @param scheduleId 日程ID
     */
    void deleteScheduleExceptionsByScheduleId(Long scheduleId);
}