package org.example.schedule.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通知服务客户端
 * 用于调用通知微服务发送通知
 */
@FeignClient(name = "notification-service", url = "http://localhost:8007") // 占位URL，后续可修改
public interface NotificationServiceClient {

    /**
     * 发送通知
     * @param userId 用户ID
     * @param content 通知内容
     * @param eventType 事件类型
     * @param channel 通知渠道
     */
    @PostMapping("/api/notifications")
    void sendNotification(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("eventType") String eventType,
            @RequestParam("channel") String channel
    );
}