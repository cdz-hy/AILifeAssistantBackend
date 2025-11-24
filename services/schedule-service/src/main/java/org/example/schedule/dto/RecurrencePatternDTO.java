package org.example.schedule.dto;

import org.example.schedule.entity.RecurrencePattern;

import java.time.DayOfWeek;
import java.util.List;

/**
 * 重复模式数据传输对象
 * 用于前后端传输重复规则数据
 */
public class RecurrencePatternDTO {
    
    /**
     * 重复频率类型
     */
    private RecurrencePattern.Frequency frequency;
    
    /**
     * 间隔数（如每2天、每3周等）
     */
    private Integer interval;
    
    /**
     * 周几（用于每周重复）
     */
    private List<DayOfWeek> daysOfWeek;
    
    /**
     * 月份（用于每年重复）
     */
    private List<Integer> months;
    
    /**
     * 具体日期（用于每月/每年重复）
     */
    private Integer dayOfMonth;
    
    // Constructors
    public RecurrencePatternDTO() {}
    
    public RecurrencePatternDTO(RecurrencePattern.Frequency frequency, Integer interval) {
        this.frequency = frequency;
        this.interval = interval;
    }
    
    // Getters and Setters
    public RecurrencePattern.Frequency getFrequency() {
        return frequency;
    }
    
    public void setFrequency(RecurrencePattern.Frequency frequency) {
        this.frequency = frequency;
    }
    
    public Integer getInterval() {
        return interval;
    }
    
    public void setInterval(Integer interval) {
        this.interval = interval;
    }
    
    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }
    
    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    
    public List<Integer> getMonths() {
        return months;
    }
    
    public void setMonths(List<Integer> months) {
        this.months = months;
    }
    
    public Integer getDayOfMonth() {
        return dayOfMonth;
    }
    
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}