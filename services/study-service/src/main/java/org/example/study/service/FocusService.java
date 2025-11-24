// service/FocusService.java
package org.example.study.service;

import org.example.study.dto.FocusRequestDTO;
import org.example.study.dto.FocusSessionDTO;
import java.util.List;

public interface FocusService {
    FocusSessionDTO startFocusSession(FocusRequestDTO request);
    FocusSessionDTO completeFocusSession(Long sessionId, Integer actualDuration);
    FocusSessionDTO interruptFocusSession(Long sessionId, Integer actualDuration);
    List<FocusSessionDTO> getUserFocusSessions(Long userId);
    Integer getTotalStudyTime(Long userId);
}