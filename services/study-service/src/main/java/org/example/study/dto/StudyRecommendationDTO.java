// dto/StudyRecommendationDTO.java
package org.example.study.dto;

public class StudyRecommendationDTO {
    private String type;
    private String title;
    private String description;
    private String priority; // HIGH, MEDIUM, LOW
    private Long relatedTaskId;

    // constructors, getters, setters
    public StudyRecommendationDTO() {}

    // getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Long getRelatedTaskId() { return relatedTaskId; }
    public void setRelatedTaskId(Long relatedTaskId) { this.relatedTaskId = relatedTaskId; }
}