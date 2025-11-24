// repository/StudyPlanRepository.java
package org.example.study.repository;

import org.example.study.entity.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    List<StudyPlan> findByUserId(Long userId);
    List<StudyPlan> findByUserIdAndStatus(Long userId, String status);

    @Query("SELECT COUNT(sp) FROM StudyPlan sp WHERE sp.userId = :userId AND sp.status = 'completed'")
    Long countCompletedPlansByUserId(Long userId);
    Long countByUserId(Long userId);
}