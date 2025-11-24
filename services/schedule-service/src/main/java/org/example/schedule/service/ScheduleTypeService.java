package org.example.schedule.service;

import org.example.schedule.entity.ScheduleType;

import java.util.List;

/**
 * 日程类型服务接口
 * 负责日程类型相关业务逻辑
 */
public interface ScheduleTypeService {
    
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
}