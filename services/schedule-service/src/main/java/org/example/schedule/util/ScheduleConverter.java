package org.example.schedule.util;

import org.example.schedule.dto.ScheduleDTO;
import org.example.schedule.entity.Schedule;

/**
 * 日程转换器
 * 用于在Schedule实体和ScheduleDTO之间进行转换
 */
public class ScheduleConverter {
    
    /**
     * 将Schedule实体转换为ScheduleDTO
     * @param entity Schedule实体
     * @return ScheduleDTO
     */
    public static ScheduleDTO toDTO(Schedule entity) {
        if (entity == null) {
            return null;
        }
        
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setTypeId(entity.getTypeId());
        dto.setUrgent(entity.getUrgent());
        dto.setImportant(entity.getImportant());
        dto.setRecurrenceRule(entity.getRecurrenceRule());
        dto.setStatus(entity.getStatus());
        
        // 处理RecurrencePattern
        if (entity.getRecurrencePattern() != null) {
            dto.setRecurrencePattern(RecurrencePatternConverter.toDTO(entity.getRecurrencePattern()));
        }
        
        return dto;
    }
    
    /**
     * 将ScheduleDTO转换为Schedule实体
     * @param dto ScheduleDTO
     * @return Schedule实体
     */
    public static Schedule toEntity(ScheduleDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Schedule entity = new Schedule();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setTypeId(dto.getTypeId());
        entity.setUrgent(dto.getUrgent());
        entity.setImportant(dto.getImportant());
        entity.setRecurrenceRule(dto.getRecurrenceRule());
        entity.setStatus(dto.getStatus());
        
        // 处理RecurrencePattern
        if (dto.getRecurrencePattern() != null) {
            entity.setRecurrencePattern(RecurrencePatternConverter.toEntity(dto.getRecurrencePattern()));
        }
        
        return entity;
    }
}