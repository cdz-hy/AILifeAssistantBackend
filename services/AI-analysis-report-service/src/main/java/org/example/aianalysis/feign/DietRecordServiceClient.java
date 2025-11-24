package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 饮食记录服务Feign客户端
 * 用于调用饮食记录服务获取用户饮食数据
 */
@FeignClient(name = "diet-record-service", url = "http://localhost:8000")
public interface DietRecordServiceClient {
    
    /**
     * 根据用户ID和日期范围获取饮食记录
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 饮食记录列表
     */
    @GetMapping("/api/diet/user/{userId}/range")
    List<Map<String, Object>> getDietRecordsByUserIdAndDateRange(
            @PathVariable("userId") Long userId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime);
}