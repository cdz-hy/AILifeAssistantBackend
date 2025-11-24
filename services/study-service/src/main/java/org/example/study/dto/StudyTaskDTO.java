// dto/StudyTaskDTO.java
package org.example.study.dto;

import java.time.LocalDate;

public class StudyTaskDTO {
    private Long id;
    private Long planId;
    private Long userId;
    private String title;
    private Boolean isCompleted;
    private LocalDate dueDate;

    // constructors, getters, setters
    public StudyTaskDTO() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}