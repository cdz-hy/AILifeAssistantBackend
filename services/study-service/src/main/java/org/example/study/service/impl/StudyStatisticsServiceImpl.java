// service/impl/StudyStatisticsServiceImpl.java
package org.example.study.service.impl;

import org.example.study.dto.StudyStatisticsDTO;
import org.example.study.entity.FocusSession;
import org.example.study.repository.FocusSessionRepository;
import org.example.study.repository.StudyPlanRepository;
import org.example.study.repository.StudyTaskRepository;
import org.example.study.service.StudyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class StudyStatisticsServiceImpl implements StudyStatisticsService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private StudyTaskRepository studyTaskRepository;

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Override
    public StudyStatisticsDTO getUserStudyStatistics(Long userId) {
        StudyStatisticsDTO statistics = new StudyStatisticsDTO();

        // 计划统计
        Long totalPlans = studyPlanRepository.countByUserId(userId);
        Long completedPlans = studyPlanRepository.countCompletedPlansByUserId(userId);

        // 任务统计
        Long totalTasks = studyTaskRepository.countByUserId(userId);
        Long completedTasks = studyTaskRepository.countCompletedTasksByUserId(userId);

        // 学习时间统计
        Integer totalStudyTime = focusSessionRepository.sumDurationByUserId(userId);

        statistics.setUserId(userId);
        statistics.setTotalPlans(totalPlans != null ? totalPlans : 0L);
        statistics.setCompletedPlans(completedPlans != null ? completedPlans : 0L);
        statistics.setTotalTasks(totalTasks != null ? totalTasks : 0L);
        statistics.setCompletedTasks(completedTasks != null ? completedTasks : 0L);
        statistics.setTotalStudyMinutes(totalStudyTime != null ? totalStudyTime : 0);
        statistics.setPlanCompletionRate(calculateCompletionRate(statistics.getTotalPlans(), statistics.getCompletedPlans()));
        statistics.setTaskCompletionRate(calculateCompletionRate(statistics.getTotalTasks(), statistics.getCompletedTasks()));

        return statistics;
    }

    @Override
    public StudyStatisticsDTO getUserStudyStatisticsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        StudyStatisticsDTO statistics = new StudyStatisticsDTO();

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        // 时间段内的专注会话
        List<FocusSession> sessionsInRange = focusSessionRepository
                .findByUserIdAndStartTimeBetween(userId, startDateTime, endDateTime);

        Integer studyTimeInRange = sessionsInRange.stream()
                .filter(session -> "completed".equals(session.getStatus()))
                .mapToInt(FocusSession::getDurationMinutes)
                .sum();

        statistics.setUserId(userId);
        statistics.setTotalStudyMinutes(studyTimeInRange);
        statistics.setFocusSessionsCount((long) sessionsInRange.size());

        return statistics;
    }

    @Override
    public StudyStatisticsDTO getPlanCompletionStatistics(Long planId) {
        StudyStatisticsDTO statistics = new StudyStatisticsDTO();

        Long totalTasksInPlan = studyTaskRepository.countByPlanId(planId);
        Long completedTasksInPlan = studyTaskRepository.countCompletedTasksByPlanId(planId);

        statistics.setTotalTasks(totalTasksInPlan != null ? totalTasksInPlan : 0L);
        statistics.setCompletedTasks(completedTasksInPlan != null ? completedTasksInPlan : 0L);
        statistics.setTaskCompletionRate(calculateCompletionRate(statistics.getTotalTasks(), statistics.getCompletedTasks()));

        return statistics;
    }

    private double calculateCompletionRate(Long total, Long completed) {
        if (total == null || total == 0) return 0.0;
        return (double) completed / total * 100;
    }
}