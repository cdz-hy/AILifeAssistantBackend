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
            notificationService.sendNotification(request);
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send notification: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@PathVariable Long userId) {
        try {
            List<NotificationResponse> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok("Notification marked as read");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to mark as read: " + e.getMessage());
        }
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long notificationId) {
        try {


            // 调用Service删除通知
            boolean deleted = notificationService.deleteNotification(notificationId);

            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "通知删除成功");
                response.put("deletedId", notificationId);

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "通知不存在或删除失败");

                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {


            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除通知失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}