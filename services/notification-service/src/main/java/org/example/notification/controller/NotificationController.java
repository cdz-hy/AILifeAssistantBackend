package org.example.notification.controller;

import org.example.notification.dto.NotificationRequest;
import org.example.notification.dto.NotificationResponse;
import org.example.notification.service.NotificationService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        try {
            System.out.println("收到发送通知请求: " + request);
            notificationService.sendNotification(request);
            System.out.println("通知发送处理完成");
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            System.err.println("发送通知失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to send notification: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Long userId) {
        try {
            System.out.println("收到获取用户通知列表请求，用户ID: " + userId);
            List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);
            System.out.println("返回通知列表，共 " + notifications.size() + " 条记录");
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.err.println("获取用户通知列表失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        try {
            System.out.println("收到标记通知为已读请求，通知ID: " + notificationId);
            notificationService.markAsRead(notificationId);
            System.out.println("标记通知为已读处理完成");
            return ResponseEntity.ok("Notification marked as read");
        } catch (Exception e) {
            System.err.println("标记通知为已读失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to mark as read: " + e.getMessage());
        }
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        try {
            System.out.println("收到标记用户所有通知为已读请求，用户ID: " + userId);
            notificationService.markAllAsRead(userId);
            System.out.println("标记用户所有通知为已读处理完成");
            return ResponseEntity.ok("All notifications marked as read");
        } catch (Exception e) {
            System.err.println("标记用户所有通知为已读失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to mark all as read: " + e.getMessage());
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long notificationId) {
        try {
            System.out.println("收到删除通知请求，通知ID: " + notificationId);

            // 调用Service删除通知
            boolean deleted = notificationService.deleteNotification(notificationId);

            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "通知删除成功");
                response.put("deletedId", notificationId);

                System.out.println("通知删除成功，通知ID: " + notificationId);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "通知不存在或删除失败");

                System.out.println("通知删除失败，通知ID: " + notificationId);
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("删除通知失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除通知失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}