package org.example.schedule.service;

import org.example.schedule.entity.ScheduleTag;

import java.util.List;

/**
 * 日程标签服务接口
 * 负责日程标签相关业务逻辑
 */
public interface ScheduleTagService {
    
    /**
     * 根据ID获取日程标签
     * @param id 标签ID
     * @return 日程标签实体
     */
    ScheduleTag getScheduleTagById(Long id);
    
    /**
     * 获取所有日程标签
     * @return 日程标签列表
     */
    List<ScheduleTag> getAllScheduleTags();
    
    /**
     * 创建日程标签
     * @param scheduleTag 日程标签实体
     * @return 创建后的日程标签实体
     */
    ScheduleTag createScheduleTag(ScheduleTag scheduleTag);
    
    /**
     * 更新日程标签
     * @param scheduleTag 日程标签实体
     * @return 更新后的日程标签实体
     */
    ScheduleTag updateScheduleTag(ScheduleTag scheduleTag);
    
    /**
     * 删除日程标签
     * @param id 标签ID
     */
    void deleteScheduleTag(Long id);
    
    /**
     * 为日程添加标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    void addTagToSchedule(Long scheduleId, Long tagId);
    
    /**
     * 移除日程的标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     */
    void removeTagFromSchedule(Long scheduleId, Long tagId);
}