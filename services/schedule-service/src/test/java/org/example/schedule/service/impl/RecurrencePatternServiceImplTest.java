package org.example.schedule.service.impl;

import org.example.schedule.entity.RecurrencePattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 重复模式服务实现类测试
 */
public class RecurrencePatternServiceImplTest {
    
    private RecurrencePatternServiceImpl recurrencePatternService;
    
    @BeforeEach
    public void setUp() {
        recurrencePatternService = new RecurrencePatternServiceImpl();
    }
    
    @Test
    public void testConvertToRRule() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.DAILY);
        pattern.setInterval(5);
        
        String rrule = recurrencePatternService.convertToRRule(pattern);
        assertEquals("RRULE:FREQ=DAILY;INTERVAL=5", rrule);
    }
    
    @Test
    public void testConvertFromRRule() {
        String rrule = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        RecurrencePattern pattern = recurrencePatternService.convertFromRRule(rrule);
        
        assertNotNull(pattern);
        assertEquals(RecurrencePattern.Frequency.WEEKLY, pattern.getFrequency());
        assertNotNull(pattern.getDaysOfWeek());
        assertEquals(3, pattern.getDaysOfWeek().size());
    }
    
    @Test
    public void testValidateValidPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.MONTHLY);
        pattern.setInterval(1);
        pattern.setDayOfMonth(15);
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertTrue(isValid);
    }
    
    @Test
    public void testValidateNullPattern() {
        boolean isValid = recurrencePatternService.validatePattern(null);
        assertFalse(isValid);
    }
    
    @Test
    public void testValidatePatternWithoutFrequency() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setInterval(2);
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertFalse(isValid);
    }
    
    @Test
    public void testValidatePatternWithInvalidInterval() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.DAILY);
        pattern.setInterval(0); // 无效的间隔
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertFalse(isValid);
    }
    
    @Test
    public void testValidatePatternWithInvalidIntervalRange() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.DAILY);
        pattern.setInterval(100); // 超出有效范围
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertFalse(isValid);
    }
    
    @Test
    public void testValidatePatternWithInvalidDayOfMonth() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.MONTHLY);
        pattern.setDayOfMonth(32); // 无效的日期
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertFalse(isValid);
    }
    
    @Test
    public void testValidatePatternWithInvalidMonth() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.YEARLY);
        pattern.setMonths(Arrays.asList(1, 13, 5)); // 13是无效的月份
        
        boolean isValid = recurrencePatternService.validatePattern(pattern);
        assertFalse(isValid);
    }
}