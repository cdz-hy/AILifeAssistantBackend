// dto/StudyStatisticsDTO.java
package org.example.study.dto;

public class StudyStatisticsDTO {
    private Long userId;
    private Long totalPlans;
    private Long completedPlans;
    private Long totalTasks;
    private Long completedTasks;
    private Integer totalStudyMinutes;
    private Double planCompletionRate;
    private Double taskCompletionRate;
    private Long focusSessionsCount;

    // constructors, getters, setters
    public StudyStatisticsDTO() {}

    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getTotalPlans() { return totalPlans; }
    public void setTotalPlans(Long totalPlans) { this.totalPlans = totalPlans; }

    public Long getCompletedPlans() { return completedPlans; }
    public void setCompletedPlans(Long completedPlans) { this.completedPlans = completedPlans; }

    public Long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Long totalTasks) { this.totalTasks = totalTasks; }

    public Long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Long completedTasks) { this.completedTasks = completedTasks; }

    public Integer getTotalStudyMinutes() { return totalStudyMinutes; }
    public void setTotalStudyMinutes(Integer totalStudyMinutes) { this.totalStudyMinutes = totalStudyMinutes; }

    public Double getPlanCompletionRate() { return planCompletionRate; }
    public void setPlanCompletionRate(Double planCompletionRate) { this.planCompletionRate = planCompletionRate; }

    public Double getTaskCompletionRate() { return taskCompletionRate; }
    public void setTaskCompletionRate(Double taskCompletionRate) { this.taskCompletionRate = taskCompletionRate; }

    public Long getFocusSessionsCount() { return focusSessionsCount; }
    public void setFocusSessionsCount(Long focusSessionsCount) { this.focusSessionsCount = focusSessionsCount; }
}