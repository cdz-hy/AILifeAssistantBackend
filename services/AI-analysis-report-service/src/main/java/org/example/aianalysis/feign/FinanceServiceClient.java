package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/api/finance/user/{userId}/range")
    List<Map<String, Object>> getFinanceRecordsByUserIdAndDateRange(
            @PathVariable("userId") Long userId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime);
}