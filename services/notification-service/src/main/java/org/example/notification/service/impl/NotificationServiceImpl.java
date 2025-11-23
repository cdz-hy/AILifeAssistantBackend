package org.example.notification.service.impl;

import org.example.notification.entity.NotificationLog;
import org.example.notification.mapper.NotificationLogMapper;
import org.example.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 通知服务实现类
 * 实现通知相关业务逻辑
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationLogMapper notificationLogMapper;

    @Transactional
    @Override
    public void logNotification(Long userId, String content, String channel, String eventType) {
        NotificationLog log = new NotificationLog();
        log.setUserId(userId);
        log.setContent(content);
        log.setChannel(channel);
        log.setStatus("sent");
        log.setSourceEventType(eventType);
        log.setCreatedAt(LocalDateTime.now());
        
        notificationLogMapper.insert(log);
    }

    @Override
    public void sendEmail(Long userId, String content) {
        // 实现邮件发送逻辑
        System.out.println("向用户 " + userId + " 发送邮件通知: " + content);
    }

    @Override
    public void sendSMS(Long userId, String content) {
        // 实现短信发送逻辑
        System.out.println("向用户 " + userId + " 发送短信通知: " + content);
    }

    @Override
    public void sendPushNotification(Long userId, String content) {
        // 实现推送通知逻辑
        System.out.println("向用户 " + userId + " 发送推送通知: " + content);
    }
}