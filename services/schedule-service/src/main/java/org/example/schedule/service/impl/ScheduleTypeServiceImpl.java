package org.example.schedule.service.impl;

import org.example.schedule.entity.ScheduleType;
import org.example.schedule.mapper.ScheduleTypeMapper;
import org.example.schedule.service.ScheduleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日程类型服务实现类
 * 实现日程类型相关业务逻辑
 */
@Service
public class ScheduleTypeServiceImpl implements ScheduleTypeService {
    
    @Autowired
    private ScheduleTypeMapper scheduleTypeMapper;
    
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
}