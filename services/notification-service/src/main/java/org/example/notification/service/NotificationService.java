package org.example.notification.service;

/**
 * 通知服务接口
 * 定义通知相关业务逻辑
 */
public interface NotificationService {

    /**
     * 记录通知日志
     * @param userId 用户ID
     * @param content 通知内容
     * @param channel 通知渠道
     * @param eventType 事件类型
     */
    void logNotification(Long userId, String content, String channel, String eventType);

    /**
     * 发送邮件通知
     * @param userId 用户ID
     * @param content 通知内容
     */
    void sendEmail(Long userId, String content);

    /**
     * 发送短信通知
     * @param userId 用户ID
     * @param content 通知内容
     */
    void sendSMS(Long userId, String content);

    /**
     * 发送推送通知
     * @param userId 用户ID
     * @param content 通知内容
     */
    void sendPushNotification(Long userId, String content);
}