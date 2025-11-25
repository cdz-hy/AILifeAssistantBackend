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
        System.out.println("开始处理通知发送请求: " + request);
        
        // 检查免打扰设置
        if (isInDndPeriod(request.getUserId())) {
            // 在免打扰期间，根据优先级决定是否发送
            if (!isHighPriority(request.getPriority())) {
                System.out.println("用户处于免打扰时段，且通知优先级较低，不发送通知");
                return; // 低优先级通知在免打扰期间不发送
            }
            System.out.println("用户处于免打扰时段，但通知优先级较高，继续发送");
        }

        // 获取用户偏好
        List<NotificationPreference> preferences = notificationMapper
                .selectPreferencesByUserAndEvent(request.getUserId(), request.getSourceEventType());
        System.out.println("获取到用户通知偏好设置: " + preferences);

        // 根据偏好决定发送渠道和方式
        String channel = determineChannel(preferences);
        String alertType = determineAlertType(preferences);
        System.out.println("确定通知渠道: " + channel + ", 提醒方式: " + alertType);

        // 创建通知日志
        NotificationLog log = new NotificationLog();
        log.setUserId(request.getUserId());
        log.setContent(request.getContent());
        log.setChannel(channel);
        log.setStatus("sent");
        log.setSourceEventType(request.getSourceEventType());
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        System.out.println("创建通知日志对象: " + log);

        int result = notificationMapper.insertNotificationLog(log);
        System.out.println("插入通知日志结果: " + result + ", 生成的通知ID: " + log.getId());

        // 实际发送通知（这里可以集成各种通知渠道）
        deliverNotification(log, alertType);
        System.out.println("通知处理完成");
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        System.out.println("获取用户通知列表，用户ID: " + userId);
        List<NotificationLog> logs = notificationMapper.selectNotificationsByUserId(userId);
        System.out.println("查询到的通知记录数: " + logs.size());
        return logs.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        System.out.println("标记通知为已读，通知ID: " + notificationId);
        notificationMapper.updateNotificationStatus(notificationId, "read");
    }

    public void markAllAsRead(Long userId) {
        System.out.println("标记用户所有通知为已读，用户ID: " + userId);
        notificationMapper.updateAllNotificationsStatusByUserId(userId, "read");
    }

    private boolean isInDndPeriod(Long userId) {
        NotificationDnd dnd = notificationMapper.selectDndByUserId(userId);
        System.out.println("获取用户免打扰设置，用户ID: " + userId + ", 设置: " + dnd);
        
        if (dnd == null || !dnd.getIsEnabled()) {
            return false;
        }

        LocalTime now = LocalTime.now();
        LocalTime start = dnd.getStartTime().toLocalTime();
        LocalTime end = dnd.getEndTime().toLocalTime();

        boolean isInDnd;
        if (start.isBefore(end)) {
            isInDnd = !now.isBefore(start) && !now.isAfter(end);
        } else {
            isInDnd = !now.isBefore(start) || !now.isAfter(end);
        }
        
        System.out.println("当前时间: " + now + ", 免打扰时段: " + start + " - " + end + ", 是否在免打扰时段: " + isInDnd);
        return isInDnd;
    }

    private boolean isHighPriority(String priority) {
        boolean isHigh = "high".equalsIgnoreCase(priority);
        System.out.println("检查通知优先级: " + priority + ", 是否为高优先级: " + isHigh);
        return isHigh;
    }

    private String determineChannel(List<NotificationPreference> preferences) {
        // 默认使用push渠道
        if (preferences == null || preferences.isEmpty()) {
            System.out.println("未找到用户偏好设置，使用默认渠道: push");
            return "push";
        }

        // 返回第一个启用的偏好设置的渠道
        String channel = preferences.stream()
                .filter(NotificationPreference::getIsEnabled)
                .map(NotificationPreference::getChannel)
                .findFirst()
                .orElse("push");
        
        System.out.println("根据用户偏好确定渠道: " + channel);
        return channel;
    }

    private String determineAlertType(List<NotificationPreference> preferences) {
        // 默认使用声音和振动
        if (preferences == null || preferences.isEmpty()) {
            System.out.println("未找到用户偏好设置，使用默认提醒方式: sound_and_vibration");
            return "sound_and_vibration";
        }

        // 返回第一个启用的偏好设置的提醒方式
        String alertType = preferences.stream()
                .filter(NotificationPreference::getIsEnabled)
                .map(NotificationPreference::getAlertType)
                .findFirst()
                .orElse("sound_and_vibration");
        
        System.out.println("根据用户偏好确定提醒方式: " + alertType);
        return alertType;
    }

    private void deliverNotification(NotificationLog log, String alertType) {
        System.out.println("开始发送通知，通知ID: " + log.getId() + ", 渠道: " + log.getChannel() + ", 提醒方式: " + alertType);
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
        NotificationResponse response = new NotificationResponse(
                log.getId(),
                log.getUserId(),
                log.getContent(),
                log.getChannel(),
                log.getStatus(),
                log.getSourceEventType(),
                log.getCreatedAt()
        );
        System.out.println("转换通知日志为响应对象: " + response);
        return response;
    }
    
    public boolean deleteNotification(Long notificationId) {
        try {
            System.out.println("删除通知，通知ID: " + notificationId);

            // 先检查通知是否存在
            NotificationLog notification = notificationMapper.selectNotificationById(notificationId);
            if (notification == null) {
                System.out.println("通知不存在，通知ID: " + notificationId);
                return false;
            }

            System.out.println("找到要删除的通知: " + notification);

            // 执行删除操作
            int result = notificationMapper.deleteNotification(notificationId);
            System.out.println("删除通知结果: " + result);

            return result > 0;
        } catch (Exception e) {
            System.err.println("删除通知失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("删除通知失败: " + e.getMessage(), e);
        }
    }
}