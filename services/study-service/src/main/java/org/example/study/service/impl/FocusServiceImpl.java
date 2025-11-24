// service/impl/FocusServiceImpl.java
package org.example.study.service.impl;

import org.example.study.dto.FocusRequestDTO;
import org.example.study.dto.FocusSessionDTO;
import org.example.study.entity.FocusSession;
import org.example.study.repository.FocusSessionRepository;
import org.example.study.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FocusServiceImpl implements FocusService {

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Override
    public FocusSessionDTO startFocusSession(FocusRequestDTO request) {
        FocusSession session = new FocusSession();
        session.setUserId(request.getUserId());
        session.setStartTime(LocalDateTime.now());
        session.setDurationMinutes(0); // 初始为0，结束时更新
        session.setStatus("in_progress");
        session.setRelatedTaskId(request.getRelatedTaskId());

        FocusSession saved = focusSessionRepository.save(session);
        return toDTO(saved);
    }

    @Override
    public FocusSessionDTO completeFocusSession(Long sessionId, Integer actualDuration) {
        return focusSessionRepository.findById(sessionId)
                .map(session -> {
                    session.setDurationMinutes(actualDuration);
                    session.setStatus("completed");
                    FocusSession updated = focusSessionRepository.save(session);
                    return toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public FocusSessionDTO interruptFocusSession(Long sessionId, Integer actualDuration) {
        return focusSessionRepository.findById(sessionId)
                .map(session -> {
                    session.setDurationMinutes(actualDuration);
                    session.setStatus("interrupted");
                    FocusSession updated = focusSessionRepository.save(session);
                    return toDTO(updated);
                })
                .orElse(null);
    }

    @Override
    public List<FocusSessionDTO> getUserFocusSessions(Long userId) {
        return focusSessionRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalStudyTime(Long userId) {
        Integer total = focusSessionRepository.sumDurationByUserId(userId);
        return total != null ? total : 0;
    }

    private FocusSessionDTO toDTO(FocusSession entity) {
        FocusSessionDTO dto = new FocusSessionDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setStartTime(entity.getStartTime());
        dto.setDurationMinutes(entity.getDurationMinutes());
        dto.setStatus(entity.getStatus());
        dto.setRelatedTaskId(entity.getRelatedTaskId());
        return dto;
    }
}