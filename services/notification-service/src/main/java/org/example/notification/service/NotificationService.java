package org.example.notification.service;


import org.example.notification.dto.NotificationRequest;
import org.example.notification.dto.NotificationResponse;
import org.example.notification.entity.NotificationLog;
import org.example.notification.entity.NotificationPreference;
import org.example.notification.entity.NotificationDnd;
import org.example.notification.mapper.NotificationMapper;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void sendNotification(NotificationRequest request) {
        // 检查免打扰设置
        if (isInDndPeriod(request.getUserId())) {
            // 在免打扰期间，根据优先级决定是否发送
            if (!isHighPriority(request.getPriority())) {
                return; // 低优先级通知在免打扰期间不发送
            }
        }

        // 获取用户偏好
        List<NotificationPreference> preferences = notificationMapper
                .selectPreferencesByUserAndEvent(request.getUserId(), request.getSourceEventType());

        // 根据偏好决定发送渠道和方式
        String channel = determineChannel(preferences);
        String alertType = determineAlertType(preferences);

        // 创建通知日志
        NotificationLog log = new NotificationLog();
        log.setUserId(request.getUserId());
        log.setContent(request.getContent());
        log.setChannel(channel);
        log.setStatus("sent");
        log.setSourceEventType(request.getSourceEventType());
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        notificationMapper.insertNotificationLog(log);

        // 实际发送通知（这里可以集成各种通知渠道）
        deliverNotification(log, alertType);
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        List<NotificationLog> logs = notificationMapper.selectNotificationsByUserId(userId);
        return logs.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        notificationMapper.updateNotificationStatus(notificationId, "read");
    }

    private boolean isInDndPeriod(Long userId) {
        NotificationDnd dnd = notificationMapper.selectDndByUserId(userId);
        if (dnd == null || !dnd.getIsEnabled()) {
            return false;
        }

        LocalTime now = LocalTime.now();
        LocalTime start = dnd.getStartTime().toLocalTime();
        LocalTime end = dnd.getEndTime().toLocalTime();

        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        } else {
            return !now.isBefore(start) || !now.isAfter(end);
        }
    }

    private boolean isHighPriority(String priority) {
        return "high".equalsIgnoreCase(priority);
    }

    private String determineChannel(List<NotificationPreference> preferences) {
        // 默认使用push渠道
        if (preferences == null || preferences.isEmpty()) {
            return "push";
        }

        // 返回第一个启用的偏好设置的渠道
        return preferences.stream()
                .filter(NotificationPreference::getIsEnabled)
                .map(NotificationPreference::getChannel)
                .findFirst()
                .orElse("push");
    }

    private String determineAlertType(List<NotificationPreference> preferences) {
        // 默认使用声音和振动
        if (preferences == null || preferences.isEmpty()) {
            return "sound_and_vibration";
        }

        // 返回第一个启用的偏好设置的提醒方式
        return preferences.stream()
                .filter(NotificationPreference::getIsEnabled)
                .map(NotificationPreference::getAlertType)
                .findFirst()
                .orElse("sound_and_vibration");
    }

    private void deliverNotification(NotificationLog log, String alertType) {
        // 这里实现实际的通知发送逻辑
        // 可以根据channel和alertType调用不同的服务
        switch (log.getChannel()) {
            case "push":
                sendPushNotification(log, alertType);
                break;
            case "websocket":
                sendWebSocketNotification(log, alertType);
                break;
            case "sms":
                sendSmsNotification(log);
                break;
            default:
                sendPushNotification(log, alertType);
        }
    }

    private void sendPushNotification(NotificationLog log, String alertType) {
        // 实现推送通知逻辑
        System.out.println("Sending push notification to user " + log.getUserId() +
                " with alert type: " + alertType);
    }

    private void sendWebSocketNotification(NotificationLog log, String alertType) {
        // 实现WebSocket通知逻辑
        System.out.println("Sending WebSocket notification to user " + log.getUserId() +
                " with alert type: " + alertType);
    }

    private void sendSmsNotification(NotificationLog log) {
        // 实现短信通知逻辑
        System.out.println("Sending SMS notification to user " + log.getUserId());
    }

    private NotificationResponse convertToResponse(NotificationLog log) {
        return new NotificationResponse(
                log.getId(),
                log.getUserId(),
                log.getContent(),
                log.getChannel(),
                log.getStatus(),
                log.getSourceEventType(),
                log.getCreatedAt()
        );
    }
    public boolean deleteNotification(Long notificationId) {
        try {


            // 先检查通知是否存在
            NotificationLog notification = notificationMapper.selectNotificationById(notificationId);
            if (notification == null) {

                return false;
            }



            // 执行删除操作
            int result = notificationMapper.deleteNotification(notificationId);


            return result > 0;
        } catch (Exception e) {

            throw new RuntimeException("删除通知失败: " + e.getMessage(), e);
        }
    }
}
