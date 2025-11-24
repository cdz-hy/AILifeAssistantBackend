// service/StudyRecommendationService.java
package org.example.study.service;

import org.example.study.dto.StudyRecommendationDTO;
import java.util.List;

public interface StudyRecommendationService {
    List<StudyRecommendationDTO> getStudyTimeRecommendations(Long userId);
    List<StudyRecommendationDTO> getTaskPriorityRecommendations(Long userId);
    List<StudyRecommendationDTO> getFocusSessionRecommendations(Long userId);
}