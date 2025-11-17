package org.example.schedule.util;

import org.example.schedule.dto.RecurrencePatternDTO;
import org.example.schedule.entity.RecurrencePattern;

/**
 * 重复模式转换器
 * 用于在RecurrencePattern实体和RecurrencePatternDTO之间进行转换
 */
public class RecurrencePatternConverter {
    
    /**
     * 将RecurrencePattern实体转换为RecurrencePatternDTO
     * @param entity RecurrencePattern实体
     * @return RecurrencePatternDTO
     */
    public static RecurrencePatternDTO toDTO(RecurrencePattern entity) {
        if (entity == null) {
            return null;
        }
        
        RecurrencePatternDTO dto = new RecurrencePatternDTO();
        dto.setFrequency(entity.getFrequency());
        dto.setInterval(entity.getInterval());
        dto.setDaysOfWeek(entity.getDaysOfWeek());
        dto.setMonths(entity.getMonths());
        dto.setDayOfMonth(entity.getDayOfMonth());
        
        return dto;
    }
    
    /**
     * 将RecurrencePatternDTO转换为RecurrencePattern实体
     * @param dto RecurrencePatternDTO
     * @return RecurrencePattern实体
     */
    public static RecurrencePattern toEntity(RecurrencePatternDTO dto) {
        if (dto == null) {
            return null;
        }
        
        RecurrencePattern entity = new RecurrencePattern();
        entity.setFrequency(dto.getFrequency());
        entity.setInterval(dto.getInterval());
        entity.setDaysOfWeek(dto.getDaysOfWeek());
        entity.setMonths(dto.getMonths());
        entity.setDayOfMonth(dto.getDayOfMonth());
        
        return entity;
    }
}