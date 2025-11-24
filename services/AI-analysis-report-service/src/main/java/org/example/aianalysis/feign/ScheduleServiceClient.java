package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 日程服务Feign客户端
 * 用于调用日程服务获取用户日程数据
 */
@FeignClient(name = "schedule-service", url = "http://localhost:8000")
public interface ScheduleServiceClient {
    
    /**
     * 根据用户ID和日期范围获取日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    @GetMapping("/schedule/api/schedules/user/{userId}/range")
    List<Map<String, Object>> getSchedulesByUserIdAndDateRange(
            @PathVariable("userId") Long userId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime);
    
    /**
     * 根据城市编码获取天气信息
     * @param cityCode 城市编码
     * @return 天气信息
     */
    @GetMapping("/schedule/api/weather/city/{cityCode}")
    Map<String, Object> getWeatherByCityCode(@PathVariable("cityCode") String cityCode);
}