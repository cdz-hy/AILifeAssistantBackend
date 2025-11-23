package org.example.schedule.service.impl;

import org.example.schedule.entity.Schedule;
import org.example.schedule.mapper.ScheduleMapper;
import org.example.schedule.service.ScheduleManagementService;
import org.example.schedule.service.EventPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程管理服务实现类
 * 实现日程核心业务逻辑
 */
@Service
public class ScheduleManagementServiceImpl implements ScheduleManagementService {
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    private EventPublishingService eventPublishingService;
    
    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleMapper.selectById(id);
    }
    
    @Override
    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Schedule> getSchedulesByUserIdAndDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleMapper.selectByUserIdAndDateRange(userId, startTime, endTime);
    }
    
    @Transactional
    @Override
    public Schedule createSchedule(Schedule schedule) {
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.insert(schedule);
        
        // Publish event
        eventPublishingService.publishScheduleEvent(schedule, "CREATED");
        
        return schedule;
    }
    
    @Transactional
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleMapper.update(schedule);
        
        // Publish event
        eventPublishingService.publishScheduleEvent(schedule, "UPDATED");
        
        return schedule;
    }
    
    @Transactional
    @Override
    public void deleteSchedule(Long id) {
        scheduleMapper.deleteById(id);
        
        // Publish event
        Schedule schedule = new Schedule();
        schedule.setId(id);
        eventPublishingService.publishScheduleEvent(schedule, "DELETED");
    }
    
    @Override
    public List<Schedule> getSchedulesByTypeId(Long typeId) {
        return scheduleMapper.selectByTypeId(typeId);
    }
    
    @Override
    public List<Schedule> getSchedulesByUserIdAndStatus(Long userId, String status) {
        return scheduleMapper.selectByUserIdAndStatus(userId, status);
    }
    
    @Override
    public List<Schedule> getSchedulesByUrgencyAndImportance(Long userId, Boolean isUrgent, Boolean isImportant) {
        return scheduleMapper.selectByUrgencyAndImportance(userId, isUrgent, isImportant);
    }
}