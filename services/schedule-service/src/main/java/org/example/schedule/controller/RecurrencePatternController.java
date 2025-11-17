package org.example.schedule.controller;

import org.example.schedule.dto.RecurrencePatternDTO;
import org.example.schedule.entity.RecurrencePattern;
import org.example.schedule.service.ScheduleManagementService;
import org.example.schedule.util.RRuleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 重复模式控制器
 * 处理重复规则相关的HTTP请求
 */
@RestController
@RequestMapping("/api/recurrence-patterns")
public class RecurrencePatternController {
    
    @Autowired
    private ScheduleManagementService scheduleManagementService;
    
    /**
     * 将RecurrencePatternDTO转换为RRULE字符串
     * @param patternDTO 重复模式DTO
     * @return RRULE字符串
     */
    @PostMapping("/to-rrule")
    public String convertToRRule(@RequestBody RecurrencePatternDTO patternDTO) {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(patternDTO.getFrequency());
        pattern.setInterval(patternDTO.getInterval());
        pattern.setDaysOfWeek(patternDTO.getDaysOfWeek());
        pattern.setMonths(patternDTO.getMonths());
        pattern.setDayOfMonth(patternDTO.getDayOfMonth());
        
        return RRuleUtils.toRRule(pattern);
    }
    
    /**
     * 将RRULE字符串转换为RecurrencePatternDTO
     * @param rrule RRULE字符串
     * @return 重复模式DTO
     */
    @PostMapping("/from-rrule")
    public RecurrencePatternDTO convertFromRRule(@RequestBody String rrule) {
        RecurrencePattern pattern = RRuleUtils.fromRRule(rrule);
        if (pattern == null) {
            return null;
        }
        
        RecurrencePatternDTO dto = new RecurrencePatternDTO();
        dto.setFrequency(pattern.getFrequency());
        dto.setInterval(pattern.getInterval());
        dto.setDaysOfWeek(pattern.getDaysOfWeek());
        dto.setMonths(pattern.getMonths());
        dto.setDayOfMonth(pattern.getDayOfMonth());
        
        return dto;
    }
}