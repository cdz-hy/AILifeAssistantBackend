package org.example.aianalysis.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 生成的分析报告实体类
 * 对应数据库表 t_generated_report
 */
public class GeneratedReport {
    private Long id;
    private Long userId;
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reportDataJson;
    private LocalDateTime createdAt;

    // Constructors
    public GeneratedReport() {}

    public GeneratedReport(Long id, Long userId, String reportType, LocalDate startDate, 
                          LocalDate endDate, String reportDataJson, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportDataJson = reportDataJson;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReportDataJson() {
        return reportDataJson;
    }

    public void setReportDataJson(String reportDataJson) {
        this.reportDataJson = reportDataJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}