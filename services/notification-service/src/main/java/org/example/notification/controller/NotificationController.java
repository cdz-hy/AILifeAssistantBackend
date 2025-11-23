package org.example.notification.controller;

import org.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 通知控制器
 * 处理通知相关的HTTP请求
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 接收并处理通知请求
     * @param userId 用户ID
     * @param content 通知内容
     * @param eventType 事件类型
     * @param channel 通知渠道
     * @return 响应结果
     */
    @PostMapping
    public ResponseEntity<String> receiveNotification(
            @RequestParam Long userId,
            @RequestParam String content,
            @RequestParam String eventType,
            @RequestParam String channel) {

        // 记录通知日志
        notificationService.logNotification(userId, content, channel, eventType);

        // 实际的通知发送逻辑（如邮件、短信、推送等）
        // notificationService.sendEmail(userId, content);
        // notificationService.sendSMS(userId, content);
        // notificationService.sendPushNotification(userId, content);

        System.out.println("接收到通知请求: 用户ID=" + userId + ", 内容=" + content + 
                           ", 事件类型=" + eventType + ", 渠道=" + channel);

        return ResponseEntity.ok("通知已接收并处理");
    }
}