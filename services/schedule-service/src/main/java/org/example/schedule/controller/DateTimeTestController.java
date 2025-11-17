package org.example.schedule.controller;

import org.example.schedule.entity.Schedule;
import org.example.schedule.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日期时间测试控制器
 * 用于验证日期时间是否能正确从数据库读取和返回
 */
@RestController
@RequestMapping("/api/test")
public class DateTimeTestController {
    
    @Autowired
    private ScheduleMapper scheduleMapper;
    
    /**
     * 根据ID获取日程并返回，用于测试时间字段是否正确读取
     * @param id 日程ID
     * @return 日程对象
     */
    @GetMapping("/schedule/{id}")
    public Schedule getSchedule(@PathVariable Long id) {
        return scheduleMapper.selectById(id);
    }
}