package org.example.study.controller;

import org.example.study.dto.StudyTaskDTO;
import org.example.study.service.StudyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-tasks")
public class StudyTaskController {

    @Autowired
    private StudyTaskService studyTaskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StudyTaskDTO>> getUserStudyTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(studyTaskService.getUserStudyTasks(userId));
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<StudyTaskDTO>> getTasksByPlan(@PathVariable Long planId) {
        return ResponseEntity.ok(studyTaskService.getTasksByPlanId(planId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyTaskDTO> getTask(@PathVariable Long id) {
        StudyTaskDTO task = studyTaskService.getStudyTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<StudyTaskDTO> createTask(@RequestBody StudyTaskDTO dto) {
        return ResponseEntity.ok(studyTaskService.createStudyTask(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyTaskDTO> updateTask(@PathVariable Long id, @RequestBody StudyTaskDTO dto) {
        StudyTaskDTO updated = studyTaskService.updateStudyTask(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return studyTaskService.deleteStudyTask(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<StudyTaskDTO> markCompleted(@PathVariable Long id) {
        StudyTaskDTO updated = studyTaskService.markTaskCompleted(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/incomplete")
    public ResponseEntity<StudyTaskDTO> markIncomplete(@PathVariable Long id) {
        StudyTaskDTO updated = studyTaskService.markTaskIncomplete(id);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}