package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务服务Feign客户端
 * 用于调用财务服务获取用户财务数据
 */
@FeignClient(name = "finance-service", url = "http://localhost:8000")
public interface FinanceServiceClient {
    
    /**
     * 根据用户ID和日期范围获取财务记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 财务记录列表
     */
    @GetMapping("/finance/api/finance/transactions")
    List<Map<String, Object>> getRecentTransactions(
            @RequestHeader("X-User-Id") Long userId);
    
    /**
     * 获取用户预算信息
     * @param userId 用户ID
     * @param year 年份
     * @param month 月份
     * @return 预算列表
     */
    @GetMapping("/finance/api/finance/budgets")
    List<Map<String, Object>> getUserBudgets(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month);
    
    /**
     * 获取月度统计信息
     * @param userId 用户ID
     * @param year 年份
     * @param month 月份
     * @return 月度统计信息
     */
    @GetMapping("/finance/api/finance/transactions/stats/monthly")
    Map<String, Object> getMonthlyStats(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month);
}