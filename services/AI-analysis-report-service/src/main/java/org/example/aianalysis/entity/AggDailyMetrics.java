package org.example.aianalysis.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 每日聚合指标实体类
 * 对应数据库表 t_agg_daily_metrics
 */
public class AggDailyMetrics {
    private Long id;
    private Long userId;
    private LocalDate date;
    private BigDecimal totalCalories;
    private Integer totalFocusMinutes;
    private BigDecimal totalExpense;

    // Constructors
    public AggDailyMetrics() {}

    public AggDailyMetrics(Long id, Long userId, LocalDate date, BigDecimal totalCalories, 
                          Integer totalFocusMinutes, BigDecimal totalExpense) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.totalCalories = totalCalories;
        this.totalFocusMinutes = totalFocusMinutes;
        this.totalExpense = totalExpense;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(BigDecimal totalCalories) {
        this.totalCalories = totalCalories;
    }

    public Integer getTotalFocusMinutes() {
        return totalFocusMinutes;
    }

    public void setTotalFocusMinutes(Integer totalFocusMinutes) {
        this.totalFocusMinutes = totalFocusMinutes;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }
}