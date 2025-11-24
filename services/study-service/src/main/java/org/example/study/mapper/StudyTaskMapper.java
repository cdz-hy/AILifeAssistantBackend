// mapper/StudyTaskMapper.java
package org.example.study.mapper;

import org.example.study.dto.StudyTaskDTO;
import org.example.study.entity.StudyTask;
import org.springframework.stereotype.Component;

@Component
public class StudyTaskMapper {

    public StudyTaskDTO toDTO(StudyTask entity) {
        if (entity == null) return null;

        StudyTaskDTO dto = new StudyTaskDTO();
        dto.setId(entity.getId());
        dto.setPlanId(entity.getPlanId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setIsCompleted(entity.getIsCompleted());
        dto.setDueDate(entity.getDueDate());
        return dto;
    }

    public StudyTask toEntity(StudyTaskDTO dto) {
        if (dto == null) return null;

        StudyTask entity = new StudyTask();
        entity.setId(dto.getId());
        entity.setPlanId(dto.getPlanId());
        entity.setUserId(dto.getUserId());
        entity.setTitle(dto.getTitle());
        entity.setIsCompleted(dto.getIsCompleted());
        entity.setDueDate(dto.getDueDate());
        return entity;
    }
}