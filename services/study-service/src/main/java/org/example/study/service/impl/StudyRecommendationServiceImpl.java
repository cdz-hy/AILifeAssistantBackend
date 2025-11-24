// service/impl/StudyRecommendationServiceImpl.java
package org.example.study.service.impl;

import org.example.study.dto.StudyRecommendationDTO;
import org.example.study.entity.FocusSession;
import org.example.study.entity.StudyTask;
import org.example.study.repository.FocusSessionRepository;
import org.example.study.repository.StudyTaskRepository;
import org.example.study.service.StudyRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyRecommendationServiceImpl implements StudyRecommendationService {

    @Autowired
    private StudyTaskRepository studyTaskRepository;

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Override
    public List<StudyRecommendationDTO> getStudyTimeRecommendations(Long userId) {
        List<StudyRecommendationDTO> recommendations = new ArrayList<>();

        // 分析用户的最佳学习时间段
        List<FocusSession> userSessions = focusSessionRepository.findByUserId(userId);

        // 按小时统计学习效率
        int[] hourProductivity = new int[24];
        for (FocusSession session : userSessions) {
            if ("completed".equals(session.getStatus())) {
                int hour = session.getStartTime().getHour();
                hourProductivity[hour] += session.getDurationMinutes();
            }
        }

        // 推荐最高效的时间段
        int bestHour = 0;
        int maxProductivity = 0;
        for (int i = 0; i < 24; i++) {
            if (hourProductivity[i] > maxProductivity) {
                maxProductivity = hourProductivity[i];
                bestHour = i;
            }
        }

        if (maxProductivity > 0) {
            StudyRecommendationDTO rec = new StudyRecommendationDTO();
            rec.setType("optimal_study_time");
            rec.setTitle("最佳学习时间推荐");
            rec.setDescription(String.format("根据您的历史数据，%d:00-%d:00 是您学习效率最高的时间段", bestHour, bestHour + 1));
            rec.setPriority("HIGH");
            recommendations.add(rec);
        }

        return recommendations;
    }

    @Override
    public List<StudyRecommendationDTO> getTaskPriorityRecommendations(Long userId) {
        List<StudyRecommendationDTO> recommendations = new ArrayList<>();

        // 获取用户未完成的任务
        List<StudyTask> incompleteTasks = studyTaskRepository.findByUserIdAndIsCompleted(userId, false);

        // 按截止日期排序，推荐即将到期的任务
        List<StudyTask> urgentTasks = incompleteTasks.stream()
                .filter(task -> task.getDueDate() != null)
                .sorted(Comparator.comparing(StudyTask::getDueDate))
                .limit(3)
                .collect(Collectors.toList());

        for (StudyTask task : urgentTasks) {
            StudyRecommendationDTO rec = new StudyRecommendationDTO();
            rec.setType("urgent_task");
            rec.setTitle("紧急任务提醒");
            rec.setDescription(String.format("任务 '%s' 即将在 %s 到期", task.getTitle(), task.getDueDate()));
            rec.setPriority("HIGH");
            rec.setRelatedTaskId(task.getId());
            recommendations.add(rec);
        }

        return recommendations;
    }

    @Override
    public List<StudyRecommendationDTO> getFocusSessionRecommendations(Long userId) {
        List<StudyRecommendationDTO> recommendations = new ArrayList<>();

        // 检查用户最近的学习模式
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<FocusSession> recentSessions = focusSessionRepository
                .findByUserIdAndStartTimeAfter(userId, oneWeekAgo);

        long completedSessions = recentSessions.stream()
                .filter(session -> "completed".equals(session.getStatus()))
                .count();

        if (completedSessions < 5) {
            StudyRecommendationDTO rec = new StudyRecommendationDTO();
            rec.setType("study_consistency");
            rec.setTitle("保持学习连续性");
            rec.setDescription("建议您每天保持至少2个专注会话，以维持学习动力");
            rec.setPriority("MEDIUM");
            recommendations.add(rec);
        }

        return recommendations;
    }
}