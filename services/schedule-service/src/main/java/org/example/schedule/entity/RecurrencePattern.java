package org.example.schedule.entity;

import java.time.DayOfWeek;
import java.util.List;

/**
 * 重复模式实体类
 * 用于表示日程的重复规则
 */
public class RecurrencePattern {
    
    /**
     * 重复频率类型
     */
    public enum Frequency {
        ONCE, // 一次性
        DAILY, // 每天
        WEEKLY, // 每周
        MONTHLY, // 每月
        YEARLY, // 每年
        CUSTOM // 自定义
    }
    
    /**
     * 重复频率
     */
    private Frequency frequency;
    
    /**
     * 间隔数（如每2年）
     */
    private Integer interval;
    
    /**
     * 周几（用于每周重复）
     */
    private List<DayOfWeek> daysOfWeek;
    
    /**
     * 月份（用于每月/每年重复）
     */
    private List<Integer> months;
    
    /**
     * 具体日期（用于每月/每年重复）
     */
    private Integer dayOfMonth;
    
    /**
     * 具体日期列表（用于每月/每年重复多个日期）
     */
    private List<Integer> daysOfMonth;
    
    /**
     * 构造函数
     */
    public RecurrencePattern() {}
    
    public RecurrencePattern(Frequency frequency, Integer interval) {
        this.frequency = frequency;
        this.interval = interval;
    }
    
    // Getters and Setters
    public Frequency getFrequency() {
        return frequency;
    }
    
    public void setFrequency(Frequency frequency) {
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
    
    public List<Integer> getDaysOfMonth() {
        return daysOfMonth;
    }
    
    public void setDaysOfMonth(List<Integer> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }
}