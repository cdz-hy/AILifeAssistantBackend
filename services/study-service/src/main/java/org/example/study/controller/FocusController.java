// controller/FocusController.java
package org.example.study.controller;

import org.example.study.dto.FocusRequestDTO;
import org.example.study.dto.FocusSessionDTO;
import org.example.study.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/focus")
public class FocusController {

    @Autowired
    private FocusService focusService;

    @PostMapping("/start")
    public ResponseEntity<FocusSessionDTO> startFocusSession(@RequestBody FocusRequestDTO request) {
        FocusSessionDTO session = focusService.startFocusSession(request);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<FocusSessionDTO> completeFocusSession(
            @PathVariable Long sessionId,
            @RequestParam Integer duration) {
        FocusSessionDTO session = focusService.completeFocusSession(sessionId, duration);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{sessionId}/interrupt")
    public ResponseEntity<FocusSessionDTO> interruptFocusSession(
            @PathVariable Long sessionId,
            @RequestParam Integer duration) {
        FocusSessionDTO session = focusService.interruptFocusSession(sessionId, duration);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}/sessions")
    public ResponseEntity<List<FocusSessionDTO>> getUserFocusSessions(@PathVariable Long userId) {
        List<FocusSessionDTO> sessions = focusService.getUserFocusSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/user/{userId}/total-time")
    public ResponseEntity<Integer> getTotalStudyTime(@PathVariable Long userId) {
        Integer totalTime = focusService.getTotalStudyTime(userId);
        return ResponseEntity.ok(totalTime);
    }
}