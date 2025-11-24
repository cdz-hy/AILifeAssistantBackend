// service/impl/StudyTaskServiceImpl.java
package org.example.study.service.impl;

import org.example.study.dto.StudyTaskDTO;
import org.example.study.entity.StudyTask;
import org.example.study.mapper.StudyTaskMapper;
import org.example.study.repository.StudyTaskRepository;
import org.example.study.service.StudyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyTaskServiceImpl implements StudyTaskService {

    @Autowired
    private StudyTaskRepository studyTaskRepository;

    @Autowired
    private StudyTaskMapper studyTaskMapper;

    @Override
    public List<StudyTaskDTO> getUserStudyTasks(Long userId) {
        return studyTaskRepository.findByUserId(userId)
                .stream()
                .map(studyTaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudyTaskDTO> getTasksByPlanId(Long planId) {
        return studyTaskRepository.findByPlanId(planId)
                .stream()
                .map(studyTaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudyTaskDTO getStudyTaskById(Long id) {
        return studyTaskRepository.findById(id)
                .map(studyTaskMapper::toDTO)
                .orElse(null);
    }

    @Override
    public StudyTaskDTO createStudyTask(StudyTaskDTO studyTaskDTO) {
        StudyTask studyTask = studyTaskMapper.toEntity(studyTaskDTO);
        StudyTask saved = studyTaskRepository.save(studyTask);
        return studyTaskMapper.toDTO(saved);
    }

    @Override
    public StudyTaskDTO updateStudyTask(Long id, StudyTaskDTO studyTaskDTO) {
        return studyTaskRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(studyTaskDTO.getTitle());
                    existing.setDueDate(studyTaskDTO.getDueDate());
                    existing.setIsCompleted(studyTaskDTO.getIsCompleted());
                    StudyTask updated = studyTaskRepository.save(existing);
                    return studyTaskMapper.toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public boolean deleteStudyTask(Long id) {
        if (studyTaskRepository.existsById(id)) {
            studyTaskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public StudyTaskDTO markTaskCompleted(Long id) {
        return studyTaskRepository.findById(id)
                .map(task -> {
                    task.setIsCompleted(true);
                    StudyTask updated = studyTaskRepository.save(task);
                    return studyTaskMapper.toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public StudyTaskDTO markTaskIncomplete(Long id) {
        return studyTaskRepository.findById(id)
                .map(task -> {
                    task.setIsCompleted(false);
                    StudyTask updated = studyTaskRepository.save(task);
                    return studyTaskMapper.toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public Long getCompletedTaskCount(Long userId) {
        return studyTaskRepository.countCompletedTasksByUserId(userId);
    }
}