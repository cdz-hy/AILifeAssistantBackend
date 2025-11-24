package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.aianalysis.dto.FinancialContextDTO;
import org.example.aianalysis.feign.FinanceServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 财务分析服务
 * 负责从财务服务获取数据并进行AI分析
 */
@Service
public class FinanceAnalysisService {
    
    @Autowired
    private FinanceServiceClient financeServiceClient;
    
    @Autowired
    private LLMClient llmClient;
    
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    /**
     * 分析用户财务并生成AI建议
     * @param userId 用户ID
     * @return AI分析结果
     */
    public String analyzeDailyFinance(Long userId) {
        try {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            
            // 1. 并行获取数据
            List<Map<String, Object>> recentTransactions = financeServiceClient.getRecentTransactions(userId);
            List<Map<String, Object>> budgets = financeServiceClient.getUserBudgets(userId, year, month);
            Map<String, Object> monthlyStats = financeServiceClient.getMonthlyStats(userId, year, month);
            
            System.out.println("获取到用户 " + userId + " 的财务数据:");
            System.out.println("近期交易: " + recentTransactions);
            System.out.println("预算信息: " + budgets);
            System.out.println("月度统计: " + monthlyStats);
            
            // 2. 数据转换与组装
            FinancialContextDTO context = new FinancialContextDTO();
            context.setCurrentDate(now);
            
            // 计算月度进度
            int daysInMonth = now.lengthOfMonth();
            int dayOfMonth = now.getDayOfMonth();
            double progress = (double) dayOfMonth / daysInMonth * 100;
            context.setMonthProgress(String.format("%.0f%%", progress) + " (" + dayOfMonth + "/" + daysInMonth + ")");
            
            // 设置月度统计数据
            FinancialContextDTO.MonthlyStats stats = new FinancialContextDTO.MonthlyStats();
            if (monthlyStats != null) {
                Object incomeObj = monthlyStats.get("totalIncome");
                Object expenseObj = monthlyStats.get("totalExpense");
                
                if (incomeObj instanceof Double) {
                    stats.setIncome(BigDecimal.valueOf((Double) incomeObj));
                } else if (incomeObj instanceof BigDecimal) {
                    stats.setIncome((BigDecimal) incomeObj);
                }
                
                if (expenseObj instanceof Double) {
                    stats.setExpense(BigDecimal.valueOf((Double) expenseObj));
                } else if (expenseObj instanceof BigDecimal) {
                    stats.setExpense((BigDecimal) expenseObj);
                }
            }
            context.setStats(stats);
            
            // 设置预算数据
            List<FinancialContextDTO.BudgetSummary> budgetSummaries = new ArrayList<>();
            if (budgets != null) {
                for (Map<String, Object> budgetMap : budgets) {
                    FinancialContextDTO.BudgetSummary budgetSummary = new FinancialContextDTO.BudgetSummary();
                    budgetSummary.setCategory((String) budgetMap.get("categoryName"));
                    
                    Object limitObj = budgetMap.get("amount");
                    Object spentObj = budgetMap.get("spentAmount");
                    
                    if (limitObj instanceof Double) {
                        budgetSummary.setLimit(BigDecimal.valueOf((Double) limitObj));
                    } else if (limitObj instanceof BigDecimal) {
                        budgetSummary.setLimit((BigDecimal) limitObj);
                    }
                    
                    if (spentObj instanceof Double) {
                        budgetSummary.setSpent(BigDecimal.valueOf((Double) spentObj));
                    } else if (spentObj instanceof BigDecimal) {
                        budgetSummary.setSpent((BigDecimal) spentObj);
                    }
                    
                    // 计算状态
                    if (budgetSummary.getLimit() != null && budgetSummary.getSpent() != null && 
                        budgetSummary.getLimit().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal percentage = budgetSummary.getSpent()
                            .multiply(BigDecimal.valueOf(100))
                            .divide(budgetSummary.getLimit(), 2, RoundingMode.HALF_UP);
                        
                        if (percentage.compareTo(BigDecimal.valueOf(90)) >= 0) {
                            budgetSummary.setStatus("CRITICAL");
                        } else if (percentage.compareTo(BigDecimal.valueOf(70)) >= 0) {
                            budgetSummary.setStatus("WARNING");
                        } else {
                            budgetSummary.setStatus("SAFE");
                        }
                    } else {
                        budgetSummary.setStatus("SAFE");
                    }
                    
                    budgetSummaries.add(budgetSummary);
                }
            }
            context.setBudgets(budgetSummaries);
            
            // 设置近期交易数据
            List<FinancialContextDTO.TransactionDTO> transactionDTOs = new ArrayList<>();
            if (recentTransactions != null) {
                for (Map<String, Object> transactionMap : recentTransactions) {
                    FinancialContextDTO.TransactionDTO transactionDTO = new FinancialContextDTO.TransactionDTO();
                    
                    // 设置日期
                    Object dateObj = transactionMap.get("transactionDate");
                    if (dateObj instanceof String) {
                        try {
                            LocalDate transactionDate = LocalDate.parse((String) dateObj, DateTimeFormatter.ISO_DATE_TIME);
                            transactionDTO.setDate(transactionDate);
                        } catch (Exception e) {
                            // 如果解析失败，跳过日期设置
                        }
                    }
                    
                    transactionDTO.setDesc((String) transactionMap.get("description"));
                    
                    Object amountObj = transactionMap.get("amount");
                    if (amountObj instanceof Double) {
                        transactionDTO.setAmount(BigDecimal.valueOf((Double) amountObj));
                    } else if (amountObj instanceof BigDecimal) {
                        transactionDTO.setAmount((BigDecimal) amountObj);
                    }
                    
                    transactionDTO.setCategory((String) transactionMap.get("categoryName"));
                    
                    transactionDTOs.add(transactionDTO);
                }
            }
            context.setRecentTransactions(transactionDTOs);
            
            // 3. 调用大模型
            String systemPrompt = getSystemPrompt();
            String userPrompt = objectMapper.writeValueAsString(context);
            
            System.out.println("=== 用户 " + userId + " 的财务分析 ===");
            System.out.println("系统提示词:");
            System.out.println(systemPrompt);
            System.out.println("\n用户提示词:");
            System.out.println(userPrompt);
            System.out.println("================================");
            
            String jsonResponse = llmClient.chat(systemPrompt, userPrompt);
            
            System.out.println("=== API 调用结果 ===");
            System.out.println("返回状态: 成功");
            System.out.println("返回数据:");
            System.out.println(jsonResponse);
            System.out.println("===================");
            
            return jsonResponse;
        } catch (Exception e) {
            System.err.println("分析用户财务时发生异常: " + e.getMessage());
            System.err.println("=== API 调用结果 ===");
            System.err.println("返回状态: 失败");
            System.err.println("错误信息:");
            e.printStackTrace();
            System.err.println("===================");
            return null;
        }
    }
    
    /**
     * 获取系统提示词
     * @return 系统提示词
     */
    private String getSystemPrompt() {
        return "你是一位犀利但富有同理心的\"私人理财顾问\"。你的目标是帮助用户实现财富增值，控制不必要的欲望。\n" +
                "你需要根据用户的【本月收支概况】、【预算执行情况】和【近期流水】，对【今日/近期】的财务状况进行分析。\n\n" +
                "分析维度：\n" +
                "1. 【预算预警】：检查是否有分类即将超支或已超支。\n" +
                "2. 【消费习惯】：从近期流水中识别不良习惯（如：高频小额消费\"拿铁效应\"、深夜冲动消费、单一类别占比过高）。\n" +
                "3. 【收支平衡】：评估本月剩余日期的可用资金压力。\n\n" +
                "输出要求：\n" +
                "返回 JSON 格式，包含 3 条核心洞察/建议。\n" +
                "格式：[{\"type\": \"warning\"|\"habit\"|\"praise\", \"title\": \"...\", \"content\": \"...\", \"action\": \"...\"}]";
    }
}