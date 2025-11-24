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
}