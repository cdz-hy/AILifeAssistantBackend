// repository/FocusSessionRepository.java
package org.example.study.repository;

import org.example.study.entity.FocusSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FocusSessionRepository extends JpaRepository<FocusSession, Long> {
    List<FocusSession> findByUserId(Long userId);
    List<FocusSession> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(fs.durationMinutes) FROM FocusSession fs WHERE fs.userId = :userId AND fs.status = 'completed'")
    Integer sumDurationByUserId(Long userId);
    List<FocusSession> findByUserIdAndStartTimeAfter(Long userId, LocalDateTime startTime);
}