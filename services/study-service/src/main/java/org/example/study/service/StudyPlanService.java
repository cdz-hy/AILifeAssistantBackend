// service/StudyPlanService.java
package org.example.study.service;

import org.example.study.dto.StudyPlanDTO;
import java.util.List;

public interface StudyPlanService {
    List<StudyPlanDTO> getUserStudyPlans(Long userId);
    StudyPlanDTO getStudyPlanById(Long id);
    StudyPlanDTO createStudyPlan(StudyPlanDTO studyPlanDTO);
    StudyPlanDTO updateStudyPlan(Long id, StudyPlanDTO studyPlanDTO);
    boolean deleteStudyPlan(Long id);
    Long getCompletedPlanCount(Long userId);
}