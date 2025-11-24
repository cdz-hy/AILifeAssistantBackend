// controller/StudyPlanController.java
package org.example.study.controller;

import org.example.study.dto.StudyPlanDTO;
import org.example.study.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-plans")
public class StudyPlanController {

    @Autowired
    private StudyPlanService studyPlanService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StudyPlanDTO>> getUserStudyPlans(@PathVariable Long userId) {
        List<StudyPlanDTO> plans = studyPlanService.getUserStudyPlans(userId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyPlanDTO> getStudyPlan(@PathVariable Long id) {
        StudyPlanDTO plan = studyPlanService.getStudyPlanById(id);
        return plan != null ? ResponseEntity.ok(plan) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<StudyPlanDTO> createStudyPlan(@RequestBody StudyPlanDTO studyPlanDTO) {
        StudyPlanDTO created = studyPlanService.createStudyPlan(studyPlanDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyPlanDTO> updateStudyPlan(@PathVariable Long id, @RequestBody StudyPlanDTO studyPlanDTO) {
        StudyPlanDTO updated = studyPlanService.updateStudyPlan(id, studyPlanDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyPlan(@PathVariable Long id) {
        boolean deleted = studyPlanService.deleteStudyPlan(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}