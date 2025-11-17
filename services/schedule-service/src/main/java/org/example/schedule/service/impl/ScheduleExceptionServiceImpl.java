package org.example.schedule.service.impl;

import org.example.schedule.entity.ScheduleException;
import org.example.schedule.mapper.ScheduleExceptionMapper;
import org.example.schedule.service.ScheduleExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日程例外服务实现类
 * 实现日程例外相关业务逻辑
 */
@Service
public class ScheduleExceptionServiceImpl implements ScheduleExceptionService {
    
    @Autowired
    private ScheduleExceptionMapper scheduleExceptionMapper;
    
    @Override
    public ScheduleException getScheduleExceptionById(Long id) {
        return scheduleExceptionMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleException> getScheduleExceptionsByScheduleId(Long scheduleId) {
        return scheduleExceptionMapper.selectByScheduleId(scheduleId);
    }
    
    @Transactional
    @Override
    public ScheduleException createScheduleException(ScheduleException scheduleException) {
        scheduleExceptionMapper.insert(scheduleException);
        return scheduleException;
    }
    
    @Transactional
    @Override
    public ScheduleException updateScheduleException(ScheduleException scheduleException) {
        scheduleExceptionMapper.update(scheduleException);
        return scheduleException;
    }
    
    @Transactional
    @Override
    public void deleteScheduleException(Long id) {
        scheduleExceptionMapper.deleteById(id);
    }
    
    @Transactional
    @Override
    public void deleteScheduleExceptionsByScheduleId(Long scheduleId) {
        scheduleExceptionMapper.deleteByScheduleId(scheduleId);
    }
}