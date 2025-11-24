package org.example.notification.service;

import org.example.notification.dto.PreferenceRequest;
import org.example.notification.entity.NotificationPreference;
import org.example.notification.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceService {

    @Autowired
    private NotificationMapper notificationMapper;

    // 设置用户偏好（新增或更新）
    public void setUserPreference(Long userId, PreferenceRequest request) {
        try {
            System.out.println("设置用户偏好 - userId: " + userId + ", request: " + request);

            NotificationPreference preference = new NotificationPreference();
            preference.setUserId(userId);
            preference.setEventType(request.getEventType());
            preference.setChannel(request.getChannel());
            preference.setAlertType(request.getAlertType());
            preference.setIsEnabled(request.getIsEnabled());

            // 先尝试更新，如果更新行数为0则插入新记录
            int updated = notificationMapper.updatePreference(preference);
            if (updated == 0) {
                System.out.println("未找到现有偏好，插入新记录");
                notificationMapper.insertPreference(preference);
            } else {
                System.out.println("更新现有偏好成功");
            }
        } catch (Exception e) {
            System.out.println("设置用户偏好失败 - userId: " + userId + ", error: " + e.getMessage());
            throw new RuntimeException("设置用户偏好失败: " + e.getMessage(), e);
        }
    }

    // 获取用户所有偏好
    public List<NotificationPreference> getUserPreferences(Long userId) {
        try {
            System.out.println("获取用户偏好 - userId: " + userId);
            List<NotificationPreference> preferences = notificationMapper.selectPreferencesByUserId(userId);
            System.out.println("找到 " + preferences.size() + " 条偏好设置");
            return preferences;
        } catch (Exception e) {
            System.out.println("获取用户偏好失败 - userId: " + userId + ", error: " + e.getMessage());
            throw new RuntimeException("获取用户偏好失败: " + e.getMessage(), e);
        }
    }

    // 删除单个偏好设置
    public boolean deletePreference(Long preferenceId) {
        try {
            System.out.println("删除偏好设置 - preferenceId: " + preferenceId);

            // 先检查偏好是否存在
            NotificationPreference preference = notificationMapper.selectPreferenceById(preferenceId);
            if (preference == null) {
                System.out.println("偏好设置不存在 - preferenceId: " + preferenceId);
                return false;
            }

            int result = notificationMapper.deletePreference(preferenceId);
            System.out.println("删除偏好设置完成 - 影响行数: " + result);
            return result > 0;
        } catch (Exception e) {
            System.out.println("删除偏好设置失败 - preferenceId: " + preferenceId + ", error: " + e.getMessage());
            throw new RuntimeException("删除偏好设置失败: " + e.getMessage(), e);
        }
    }

    // 删除用户的所有偏好设置
    public int deleteAllUserPreferences(Long userId) {
        try {
            System.out.println("删除用户所有偏好设置 - userId: " + userId);
            int result = notificationMapper.deletePreferencesByUserId(userId);
            System.out.println("删除用户所有偏好设置完成 - 影响行数: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("删除用户所有偏好设置失败 - userId: " + userId + ", error: " + e.getMessage());
            throw new RuntimeException("删除用户所有偏好设置失败: " + e.getMessage(), e);
        }
    }
}