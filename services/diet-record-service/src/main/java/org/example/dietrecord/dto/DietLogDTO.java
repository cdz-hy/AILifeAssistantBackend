package org.example.dietrecord.dto;

import java.time.LocalDate;
import java.util.List;

public class DietLogDTO {
    private Long id;
    private Long userId;
    private LocalDate logDate;
    private List<DietEntryDTO> entries;
    private DailyNutritionStatsDTO nutritionStats;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public List<DietEntryDTO> getEntries() { return entries; }
    public void setEntries(List<DietEntryDTO> entries) { this.entries = entries; }

    public DailyNutritionStatsDTO getNutritionStats() { return nutritionStats; }
    public void setNutritionStats(DailyNutritionStatsDTO nutritionStats) { this.nutritionStats = nutritionStats; }
}

