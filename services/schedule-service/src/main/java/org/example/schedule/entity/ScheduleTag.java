package org.example.schedule.entity;

public class ScheduleTag {
    private Long id;
    private String tagName;
    private String createdBy;

    // Constructors
    public ScheduleTag() {}

    public ScheduleTag(Long id, String tagName, String createdBy) {
        this.id = id;
        this.tagName = tagName;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}