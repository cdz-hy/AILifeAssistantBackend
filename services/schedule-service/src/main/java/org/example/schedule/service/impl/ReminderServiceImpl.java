package org.example.schedule.service.impl;

import org.example.schedule.entity.ScheduleReminder;
import org.example.schedule.mapper.ScheduleReminderMapper;
import org.example.schedule.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒服务实现类
 * 实现日程提醒相关业务逻辑
 */
@Service
public class ReminderServiceImpl implements ReminderService {
    
    @Autowired
    private ScheduleReminderMapper scheduleReminderMapper;
    
    @Override
    public ScheduleReminder getReminderById(Long id) {
        return scheduleReminderMapper.selectById(id);
    }
    
    @Override
    public List<ScheduleReminder> getRemindersByScheduleId(Long scheduleId) {
        return scheduleReminderMapper.selectByScheduleId(scheduleId);
    }
    
    @Override
    public List<ScheduleReminder> getPendingReminders() {
        return scheduleReminderMapper.selectPendingReminders(LocalDateTime.now());
    }
    
    @Transactional
    @Override
    public ScheduleReminder createReminder(ScheduleReminder reminder) {
        scheduleReminderMapper.insert(reminder);
        return reminder;
    }
    
    @Transactional
    @Override
    public ScheduleReminder updateReminder(ScheduleReminder reminder) {
        scheduleReminderMapper.update(reminder);
        return reminder;
    }
    
    @Transactional
    @Override
    public void deleteReminder(Long id) {
        scheduleReminderMapper.deleteById(id);
    }
    
    @Transactional
    @Override
    public void deleteRemindersByScheduleId(Long scheduleId) {
        scheduleReminderMapper.deleteByScheduleId(scheduleId);
    }
}