package org.example.aianalysis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 财务分析上下文DTO
 * 用于封装发送给大语言模型的财务数据
 */
public class FinancialContextDTO {
    @JsonProperty("current_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate currentDate;
    
    @JsonProperty("month_progress")
    private String monthProgress;
    
    private MonthlyStats stats;
    private List<BudgetSummary> budgets;
    @JsonProperty("recent_transactions")
    private List<TransactionDTO> recentTransactions;
    
    // Getters and Setters
    public LocalDate getCurrentDate() {
        return currentDate;
    }
    
    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
    
    public String getMonthProgress() {
        return monthProgress;
    }
    
    public void setMonthProgress(String monthProgress) {
        this.monthProgress = monthProgress;
    }
    
    public MonthlyStats getStats() {
        return stats;
    }
    
    public void setStats(MonthlyStats stats) {
        this.stats = stats;
    }
    
    public List<BudgetSummary> getBudgets() {
        return budgets;
    }
    
    public void setBudgets(List<BudgetSummary> budgets) {
        this.budgets = budgets;
    }
    
    public List<TransactionDTO> getRecentTransactions() {
        return recentTransactions;
    }
    
    public void setRecentTransactions(List<TransactionDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
    
    /**
     * 月度统计信息
     */
    public static class MonthlyStats {
        private BigDecimal income;
        private BigDecimal expense;
        
        public BigDecimal getIncome() {
            return income;
        }
        
        public void setIncome(BigDecimal income) {
            this.income = income;
        }
        
        public BigDecimal getExpense() {
            return expense;
        }
        
        public void setExpense(BigDecimal expense) {
            this.expense = expense;
        }
    }
    
    /**
     * 预算摘要
     */
    public static class BudgetSummary {
        private String category;
        private BigDecimal limit;
        private BigDecimal spent;
        private String status; // SAFE, WARNING, CRITICAL
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
        
        public BigDecimal getLimit() {
            return limit;
        }
        
        public void setLimit(BigDecimal limit) {
            this.limit = limit;
        }
        
        public BigDecimal getSpent() {
            return spent;
        }
        
        public void setSpent(BigDecimal spent) {
            this.spent = spent;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
    
    /**
     * 交易信息
     */
    public static class TransactionDTO {
        @JsonFormat(pattern = "MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        private LocalDate date;
        private String desc;
        private BigDecimal amount;
        private String category;
        
        public LocalDate getDate() {
            return date;
        }
        
        public void setDate(LocalDate date) {
            this.date = date;
        }
        
        public String getDesc() {
            return desc;
        }
        
        public void setDesc(String desc) {
            this.desc = desc;
        }
        
        public BigDecimal getAmount() {
            return amount;
        }
        
        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
        
        public String getCategory() {
            return category;
        }
        
        public void setCategory(String category) {
            this.category = category;
        }
    }
}