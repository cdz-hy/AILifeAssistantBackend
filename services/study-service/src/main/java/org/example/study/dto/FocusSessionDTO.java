// dto/FocusSessionDTO.java
package org.example.study.dto;

import java.time.LocalDateTime;

public class FocusSessionDTO {
    private Long id;
    private Long userId;
    private LocalDateTime startTime;
    private Integer durationMinutes;
    private String status;
    private Long relatedTaskId;

    // constructors, getters, setters
    public FocusSessionDTO() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getRelatedTaskId() { return relatedTaskId; }
    public void setRelatedTaskId(Long relatedTaskId) { this.relatedTaskId = relatedTaskId; }
}