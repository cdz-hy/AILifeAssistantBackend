package org.example.schedule.entity;

public class ScheduleAISuggestion {
    private Long id;
    private Long scheduleId;
    private String suggestionType;
    private String suggestionContent;
    private Boolean isAccepted;

    // Constructors
    public ScheduleAISuggestion() {}

    public ScheduleAISuggestion(Long id, Long scheduleId, String suggestionType, String suggestionContent, Boolean isAccepted) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.suggestionType = suggestionType;
        this.suggestionContent = suggestionContent;
        this.isAccepted = isAccepted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getSuggestionContent() {
        return suggestionContent;
    }

    public void setSuggestionContent(String suggestionContent) {
        this.suggestionContent = suggestionContent;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}