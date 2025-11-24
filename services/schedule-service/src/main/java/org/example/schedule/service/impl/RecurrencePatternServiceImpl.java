package org.example.schedule.service.impl;

import org.example.schedule.entity.RecurrencePattern;
import org.example.schedule.service.RecurrencePatternService;
import org.example.schedule.util.RRuleUtils;
import org.springframework.stereotype.Service;

/**
 * 重复模式服务实现类
 * 实现重复规则相关业务逻辑
 */
@Service
public class RecurrencePatternServiceImpl implements RecurrencePatternService {
    
    @Override
    public String convertToRRule(RecurrencePattern pattern) {
        return RRuleUtils.toRRule(pattern);
    }
    
    @Override
    public RecurrencePattern convertFromRRule(String rrule) {
        return RRuleUtils.fromRRule(rrule);
    }
    
    @Override
    public boolean validatePattern(RecurrencePattern pattern) {
        if (pattern == null) {
            return false;
        }
        
        // 检查频率类型是否有效
        if (pattern.getFrequency() == null) {
            return false;
        }
        
        // 检查间隔是否有效（如果设置）
        if (pattern.getInterval() != null && pattern.getInterval() < 1) {
            return false;
        }
        
        // 检查间隔是否在有效范围内（1-99）
        if (pattern.getInterval() != null && (pattern.getInterval() < 1 || pattern.getInterval() > 99)) {
            return false;
        }
        
        // 检查星期几是否有效（如果设置）
        if (pattern.getDaysOfWeek() != null) {
            for (java.time.DayOfWeek day : pattern.getDaysOfWeek()) {
                if (day == null) {
                    return false;
                }
            }
        }
        
        // 检查月份是否有效（如果设置）
        if (pattern.getMonths() != null) {
            for (Integer month : pattern.getMonths()) {
                if (month == null || month < 1 || month > 12) {
                    return false;
                }
            }
        }
        
        // 检查日期是否有效（如果设置）
        if (pattern.getDayOfMonth() != null && (pattern.getDayOfMonth() < 1 || pattern.getDayOfMonth() > 31)) {
            return false;
        }
        
        return true;
    }
}