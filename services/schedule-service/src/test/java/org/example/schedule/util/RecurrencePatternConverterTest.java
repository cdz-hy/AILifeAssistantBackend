package org.example.schedule.util;

import org.example.schedule.dto.RecurrencePatternDTO;
import org.example.schedule.entity.RecurrencePattern;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 重复模式转换器测试
 */
public class RecurrencePatternConverterTest {
    
    @Test
    public void testConvertToDTO() {
        RecurrencePattern entity = new RecurrencePattern();
        entity.setFrequency(RecurrencePattern.Frequency.WEEKLY);
        entity.setInterval(2);
        entity.setDaysOfWeek(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
        entity.setMonths(Arrays.asList(1, 6, 12));
        entity.setDayOfMonth(15);
        
        RecurrencePatternDTO dto = RecurrencePatternConverter.toDTO(entity);
        
        assertNotNull(dto);
        assertEquals(RecurrencePattern.Frequency.WEEKLY, dto.getFrequency());
        assertEquals(Integer.valueOf(2), dto.getInterval());
        assertEquals(entity.getDaysOfWeek(), dto.getDaysOfWeek());
        assertEquals(entity.getMonths(), dto.getMonths());
        assertEquals(Integer.valueOf(15), dto.getDayOfMonth());
    }
    
    @Test
    public void testConvertToEntity() {
        RecurrencePatternDTO dto = new RecurrencePatternDTO();
        dto.setFrequency(RecurrencePattern.Frequency.MONTHLY);
        dto.setInterval(3);
        dto.setDaysOfWeek(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        dto.setMonths(Arrays.asList(3, 9));
        dto.setDayOfMonth(10);
        
        RecurrencePattern entity = RecurrencePatternConverter.toEntity(dto);
        
        assertNotNull(entity);
        assertEquals(RecurrencePattern.Frequency.MONTHLY, entity.getFrequency());
        assertEquals(Integer.valueOf(3), entity.getInterval());
        assertEquals(dto.getDaysOfWeek(), entity.getDaysOfWeek());
        assertEquals(dto.getMonths(), entity.getMonths());
        assertEquals(Integer.valueOf(10), entity.getDayOfMonth());
    }
    
    @Test
    public void testConvertNullEntity() {
        RecurrencePatternDTO dto = RecurrencePatternConverter.toDTO(null);
        assertNull(dto);
    }
    
    @Test
    public void testConvertNullDTO() {
        RecurrencePattern entity = RecurrencePatternConverter.toEntity(null);
        assertNull(entity);
    }
}