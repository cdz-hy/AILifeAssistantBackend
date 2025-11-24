// mapper/StudyPlanMapper.java
package org.example.study.mapper;

import org.example.study.dto.StudyPlanDTO;
import org.example.study.entity.StudyPlan;
import org.springframework.stereotype.Component;

@Component
public class StudyPlanMapper {

    public StudyPlanDTO toDTO(StudyPlan entity) {
        if (entity == null) return null;

        StudyPlanDTO dto = new StudyPlanDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setGoal(entity.getGoal());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public StudyPlan toEntity(StudyPlanDTO dto) {
        if (dto == null) return null;

        StudyPlan entity = new StudyPlan();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setTitle(dto.getTitle());
        entity.setGoal(dto.getGoal());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}