package org.example.schedule.service;

import org.example.schedule.entity.RecurrencePattern;

/**
 * 重复模式服务接口
 * 定义重复规则相关业务逻辑
 */
public interface RecurrencePatternService {
    
    /**
     * 将RecurrencePattern转换为RRULE字符串
     * @param pattern 重复模式
     * @return RRULE字符串
     */
    String convertToRRule(RecurrencePattern pattern);
    
    /**
     * 将RRULE字符串转换为RecurrencePattern
     * @param rrule RRULE字符串
     * @return 重复模式
     */
    RecurrencePattern convertFromRRule(String rrule);
    
    /**
     * 验证重复模式是否有效
     * @param pattern 重复模式
     * @return 是否有效
     */
    boolean validatePattern(RecurrencePattern pattern);
}