package org.example.finance.dto;

import java.math.BigDecimal;
import java.util.Map;

public class MonthlyStatsDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Map<String, CategoryStat> categoryStats;

    public static class CategoryStat {
        private BigDecimal amount;
        private Double increaseRate;

        public CategoryStat() {}

        public CategoryStat(BigDecimal amount, Double increaseRate) {
            this.amount = amount;
            this.increaseRate = increaseRate;
        }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public Double getIncreaseRate() { return increaseRate; }
        public void setIncreaseRate(Double increaseRate) { this.increaseRate = increaseRate; }
    }

    // 默认构造器
    public MonthlyStatsDTO() {}

    // 带参数的构造器
    public MonthlyStatsDTO(BigDecimal totalIncome, BigDecimal totalExpense, Map<String, CategoryStat> categoryStats) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.categoryStats = categoryStats;
    }

    // Getter 和 Setter 方法
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public Map<String, CategoryStat> getCategoryStats() { return categoryStats; }
    public void setCategoryStats(Map<String, CategoryStat> categoryStats) { this.categoryStats = categoryStats; }
}