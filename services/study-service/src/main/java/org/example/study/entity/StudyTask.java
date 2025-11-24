// entity/StudyTask.java
package org.example.study.entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "t_study_task")
public class StudyTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // constructors
    public StudyTask() {}

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