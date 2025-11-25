package org.example.schedule.service;

import org.example.notification.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 通知服务客户端
 * 用于调用通知微服务发送通知
 */
@FeignClient(name = "notification-service", path = "/api/notifications") 
public interface NotificationServiceClient {

    /**
     * 发送通知
     * @param request 通知请求
     */
    @PostMapping("/send")
    void sendNotification(@RequestBody NotificationRequest request);
}