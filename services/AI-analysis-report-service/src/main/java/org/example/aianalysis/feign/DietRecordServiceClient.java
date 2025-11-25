package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * 饮食记录服务Feign客户端
 * 用于调用饮食记录服务获取用户饮食数据
 */
@FeignClient(name = "diet-record-service", url = "http://localhost:8000")
public interface DietRecordServiceClient {
    
    /**
     * 根据用户ID和日期获取饮食日志
     * @param userId 用户ID
     * @param date 日期 (yyyy-MM-dd)
     * @return 饮食日志
     */
    @GetMapping("/diet/api/diet-logs/{userId}/{date}")
    Map<String, Object> getDietLog(
            @PathVariable("userId") Long userId,
            @PathVariable("date") String date);

    /**
     * 根据用户ID和日期获取每日营养统计
     * @param userId 用户ID
     * @param date 日期 (yyyy-MM-dd)
     * @return 营养统计
     */
    @GetMapping("/diet/api/diet-logs/{userId}/{date}/stats")
    Map<String, Object> getDailyNutritionStats(
            @PathVariable("userId") Long userId,
            @PathVariable("date") String date);
}