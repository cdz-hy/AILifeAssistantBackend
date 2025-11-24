// dto/FocusRequestDTO.java
package org.example.study.dto;

public class FocusRequestDTO {
    private Long userId;
    private Long relatedTaskId;
    private Integer plannedDuration; // 计划专注时长（分钟）

    // constructors, getters, setters
    public FocusRequestDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRelatedTaskId() { return relatedTaskId; }
    public void setRelatedTaskId(Long relatedTaskId) { this.relatedTaskId = relatedTaskId; }

    public Integer getPlannedDuration() { return plannedDuration; }
    public void setPlannedDuration(Integer plannedDuration) { this.plannedDuration = plannedDuration; }
}