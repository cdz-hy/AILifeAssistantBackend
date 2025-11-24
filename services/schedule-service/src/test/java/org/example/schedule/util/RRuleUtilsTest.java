package org.example.schedule.util;

import org.example.schedule.entity.RecurrencePattern;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RRule工具类测试
 */
public class RRuleUtilsTest {
    
    @Test
    public void testOncePattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.ONCE);
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertNull(rrule);
    }
    
    @Test
    public void testDailyPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.DAILY);
        pattern.setInterval(2);
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertEquals("RRULE:FREQ=DAILY;INTERVAL=2", rrule);
    }
    
    @Test
    public void testWeeklyPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.WEEKLY);
        pattern.setInterval(1);
        pattern.setDaysOfWeek(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertEquals("RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR", rrule);
    }
    
    @Test
    public void testMonthlyPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.MONTHLY);
        pattern.setInterval(1);
        pattern.setDayOfMonth(15);
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertEquals("RRULE:FREQ=MONTHLY;BYMONTHDAY=15", rrule);
    }
    
    @Test
    public void testYearlyPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.YEARLY);
        pattern.setInterval(1);
        pattern.setMonths(Arrays.asList(1, 3, 5));
        pattern.setDayOfMonth(1);
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertEquals("RRULE:FREQ=YEARLY;BYMONTH=1,3,5;BYMONTHDAY=1", rrule);
    }
    
    @Test
    public void testCustomPattern() {
        RecurrencePattern pattern = new RecurrencePattern();
        pattern.setFrequency(RecurrencePattern.Frequency.CUSTOM);
        pattern.setInterval(3);
        pattern.setDaysOfWeek(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        
        String rrule = RRuleUtils.toRRule(pattern);
        assertEquals("RRULE:FREQ=WEEKLY;INTERVAL=3;BYDAY=TU,TH", rrule);
    }
    
    @Test
    public void testParseDailyRRule() {
        String rrule = "RRULE:FREQ=DAILY;INTERVAL=2";
        RecurrencePattern pattern = RRuleUtils.fromRRule(rrule);
        
        assertNotNull(pattern);
        assertEquals(RecurrencePattern.Frequency.DAILY, pattern.getFrequency());
        assertEquals(Integer.valueOf(2), pattern.getInterval());
    }
    
    @Test
    public void testParseWeeklyRRule() {
        String rrule = "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        RecurrencePattern pattern = RRuleUtils.fromRRule(rrule);
        
        assertNotNull(pattern);
        assertEquals(RecurrencePattern.Frequency.WEEKLY, pattern.getFrequency());
        assertNotNull(pattern.getDaysOfWeek());
        assertEquals(3, pattern.getDaysOfWeek().size());
        assertTrue(pattern.getDaysOfWeek().contains(DayOfWeek.MONDAY));
        assertTrue(pattern.getDaysOfWeek().contains(DayOfWeek.WEDNESDAY));
        assertTrue(pattern.getDaysOfWeek().contains(DayOfWeek.FRIDAY));
    }
    
    @Test
    public void testParseMonthlyRRule() {
        String rrule = "RRULE:FREQ=MONTHLY;BYMONTHDAY=15";
        RecurrencePattern pattern = RRuleUtils.fromRRule(rrule);
        
        assertNotNull(pattern);
        assertEquals(RecurrencePattern.Frequency.MONTHLY, pattern.getFrequency());
        assertEquals(Integer.valueOf(15), pattern.getDayOfMonth());
    }
    
    @Test
    public void testParseYearlyRRule() {
        String rrule = "RRULE:FREQ=YEARLY;BYMONTH=1,3,5;BYMONTHDAY=1";
        RecurrencePattern pattern = RRuleUtils.fromRRule(rrule);
        
        assertNotNull(pattern);
        assertEquals(RecurrencePattern.Frequency.YEARLY, pattern.getFrequency());
        assertEquals(Integer.valueOf(1), pattern.getDayOfMonth());
        assertNotNull(pattern.getMonths());
        assertEquals(3, pattern.getMonths().size());
        assertTrue(pattern.getMonths().contains(1));
        assertTrue(pattern.getMonths().contains(3));
        assertTrue(pattern.getMonths().contains(5));
    }
}