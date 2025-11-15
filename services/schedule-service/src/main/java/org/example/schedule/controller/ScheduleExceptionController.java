package org.example.schedule.controller;

import org.example.schedule.entity.ScheduleException;
import org.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日程例外控制器
 * 处理日程例外相关的HTTP请求
 */
@RestController
@RequestMapping("/api/schedule-exceptions")
public class ScheduleExceptionController {
    
    @Autowired
    private ScheduleService scheduleService;
    
    /**
     * 根据ID获取日程例外
     * @param id 例外ID
     * @return 日程例外实体
     */
    @GetMapping("/{id}")
    public ScheduleException getScheduleExceptionById(@PathVariable Long id) {
        return scheduleService.getScheduleExceptionById(id);
    }
    
    /**
     * 根据日程ID获取例外
     * @param scheduleId 日程ID
     * @return 日程例外列表
     */
    @GetMapping("/schedule/{scheduleId}")
    public List<ScheduleException> getScheduleExceptionsByScheduleId(@PathVariable Long scheduleId) {
        return scheduleService.getScheduleExceptionsByScheduleId(scheduleId);
    }
    
    /**
     * 创建日程例外
     * @param scheduleException 日程例外实体
     * @return 创建后的日程例外实体
     */
    @PostMapping
    public ScheduleException createScheduleException(@RequestBody ScheduleException scheduleException) {
        return scheduleService.createScheduleException(scheduleException);
    }
    
    /**
     * 更新日程例外
     * @param id 例外ID
     * @param scheduleException 日程例外实体
     * @return 更新后的日程例外实体
     */
    @PutMapping("/{id}")
    public ScheduleException updateScheduleException(@PathVariable Long id, @RequestBody ScheduleException scheduleException) {
        scheduleException.setId(id);
        return scheduleService.updateScheduleException(scheduleException);
    }
    
    /**
     * 删除日程例外
     * @param id 例外ID
     */
    @DeleteMapping("/{id}")
    public void deleteScheduleException(@PathVariable Long id) {
        scheduleService.deleteScheduleException(id);
    }
}