// service/StudyTaskService.java
package org.example.study.service;

import org.example.study.dto.StudyTaskDTO;
import java.util.List;

public interface StudyTaskService {
    List<StudyTaskDTO> getUserStudyTasks(Long userId);
    List<StudyTaskDTO> getTasksByPlanId(Long planId);
    StudyTaskDTO getStudyTaskById(Long id);
    StudyTaskDTO createStudyTask(StudyTaskDTO studyTaskDTO);
    StudyTaskDTO updateStudyTask(Long id, StudyTaskDTO studyTaskDTO);
    boolean deleteStudyTask(Long id);
    StudyTaskDTO markTaskCompleted(Long id);
    StudyTaskDTO markTaskIncomplete(Long id);
    Long getCompletedTaskCount(Long userId);
}