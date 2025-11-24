// dto/StudyPlanDTO.java
package org.example.study.dto;

import java.time.LocalDate;

public class StudyPlanDTO {
    private Long id;
    private Long userId;
    private String title;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // constructors, getters, setters
    public StudyPlanDTO() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}