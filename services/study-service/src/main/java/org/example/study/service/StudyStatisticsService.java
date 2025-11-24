// service/StudyStatisticsService.java
package org.example.study.service;

import org.example.study.dto.StudyStatisticsDTO;
import java.time.LocalDate;

public interface StudyStatisticsService {
    StudyStatisticsDTO getUserStudyStatistics(Long userId);
    StudyStatisticsDTO getUserStudyStatisticsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    StudyStatisticsDTO getPlanCompletionStatistics(Long planId);
}