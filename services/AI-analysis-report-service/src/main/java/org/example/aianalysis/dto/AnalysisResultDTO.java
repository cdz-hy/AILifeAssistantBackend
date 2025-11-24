package org.example.aianalysis.dto;

/**
 * AI分析结果DTO
 * 用于封装大语言模型返回的分析结果
 */
public class AnalysisResultDTO {
    private String type;
    private String riskLevel;
    private String title;
    private String content;
    private Long relatedScheduleId;
    
    // Getters and Setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getRiskLevel() {
        return riskLevel;
    }
    
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getRelatedScheduleId() {
        return relatedScheduleId;
    }
    
    public void setRelatedScheduleId(Long relatedScheduleId) {
        this.relatedScheduleId = relatedScheduleId;
    }
}