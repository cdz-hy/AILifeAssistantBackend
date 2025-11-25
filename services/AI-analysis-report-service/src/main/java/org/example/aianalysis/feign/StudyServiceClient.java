package org.example.aianalysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 学习服务Feign客户端
 * 用于调用学习服务获取用户学习数据
 */
@FeignClient(name = "study-service", url = "http://localhost:8000")
public interface StudyServiceClient {
    
    /**
     * 根据用户ID获取学习记录
     * @param userId 用户ID
     * @return 学习记录列表
     */
    @GetMapping("/api/study/user/{userId}")
    List<Map<String, Object>> getStudyRecordsByUserId(@PathVariable("userId") Long userId);
    
    /**
     * 获取用户的专注会话列表
     * @param userId 用户ID
     * @return 专注会话列表
     */
    @GetMapping("/study/api/focus/user/{userId}/sessions")
    List<Map<String, Object>> getUserFocusSessions(@PathVariable("userId") Long userId);
    
    /**
     * 获取用户的总学习时长（分钟）
     * @param userId 用户ID
     * @return 总学习时长
     */
    @GetMapping("/study/api/focus/user/{userId}/total-time")
    Integer getTotalStudyTime(@PathVariable("userId") Long userId);
    
    /**
     * 获取用户的学习任务列表
     * @param userId 用户ID
     * @return 学习任务列表
     */
    @GetMapping("/study/api/study-tasks/user/{userId}")
    List<Map<String, Object>> getUserStudyTasks(@PathVariable("userId") Long userId);
}