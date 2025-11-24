// repository/StudyTaskRepository.java
package org.example.study.repository;

import org.example.study.entity.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    List<StudyTask> findByUserId(Long userId);
    List<StudyTask> findByPlanId(Long planId);
    List<StudyTask> findByUserIdAndIsCompleted(Long userId, Boolean isCompleted);

    @Query("SELECT COUNT(st) FROM StudyTask st WHERE st.userId = :userId AND st.isCompleted = true")
    Long countCompletedTasksByUserId(Long userId);
    Long countByUserId(Long userId);
    Long countByPlanId(Long planId);
    Long countCompletedTasksByPlanId(Long planId);
}