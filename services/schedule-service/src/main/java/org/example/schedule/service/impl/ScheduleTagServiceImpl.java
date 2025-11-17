package org.example.schedule.service.impl;

import org.example.schedule.entity.ScheduleTag;
import org.example.schedule.mapper.ScheduleTagMapper;
import org.example.schedule.mapper.ScheduleTagMapMapper;
import org.example.schedule.service.ScheduleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日程标签服务实现类
 * 实现日程标签相关业务逻辑
 */
@Service
public class ScheduleTagServiceImpl implements ScheduleTagService {
    
    @Autowired
    private ScheduleTagMapper scheduleTagMapper;
    
    @Autowired
    private ScheduleTagMapMapper scheduleTagMapMapper;
    
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
}