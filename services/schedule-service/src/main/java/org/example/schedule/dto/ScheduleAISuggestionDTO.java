package org.example.schedule.dto;

public class ScheduleAISuggestionDTO {
    private Long id;
    private Long scheduleId;
    private String suggestionType;
    private String suggestionContent;
    private Boolean isAccepted;
    
    // Constructors
    public ScheduleAISuggestionDTO() {}
    
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