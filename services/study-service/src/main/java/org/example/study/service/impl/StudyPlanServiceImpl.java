// service/impl/StudyPlanServiceImpl.java
package org.example.study.service.impl;

import org.example.study.dto.StudyPlanDTO;
import org.example.study.entity.StudyPlan;
import org.example.study.mapper.StudyPlanMapper;
import org.example.study.repository.StudyPlanRepository;
import org.example.study.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private StudyPlanMapper studyPlanMapper;

    @Override
    public List<StudyPlanDTO> getUserStudyPlans(Long userId) {
        return studyPlanRepository.findByUserId(userId)
                .stream()
                .map(studyPlanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudyPlanDTO getStudyPlanById(Long id) {
        return studyPlanRepository.findById(id)
                .map(studyPlanMapper::toDTO)
                .orElse(null);
    }

    @Override
    public StudyPlanDTO createStudyPlan(StudyPlanDTO studyPlanDTO) {
        StudyPlan studyPlan = studyPlanMapper.toEntity(studyPlanDTO);
        StudyPlan saved = studyPlanRepository.save(studyPlan);
        return studyPlanMapper.toDTO(saved);
    }

    @Override
    public StudyPlanDTO updateStudyPlan(Long id, StudyPlanDTO studyPlanDTO) {
        return studyPlanRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(studyPlanDTO.getTitle());
                    existing.setGoal(studyPlanDTO.getGoal());
                    existing.setStartDate(studyPlanDTO.getStartDate());
                    existing.setEndDate(studyPlanDTO.getEndDate());
                    existing.setStatus(studyPlanDTO.getStatus());
                    StudyPlan updated = studyPlanRepository.save(existing);
                    return studyPlanMapper.toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public boolean deleteStudyPlan(Long id) {
        if (studyPlanRepository.existsById(id)) {
            studyPlanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Long getCompletedPlanCount(Long userId) {
        return studyPlanRepository.countCompletedPlansByUserId(userId);
    }
}