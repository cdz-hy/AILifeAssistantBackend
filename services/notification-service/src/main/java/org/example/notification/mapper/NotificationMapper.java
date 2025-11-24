package org.example.notification.mapper;


import org.example.notification.entity.NotificationLog;
import org.example.notification.entity.NotificationPreference;
import org.example.notification.entity.NotificationDnd;
import org.example.notification.dto.NotificationResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    // Notification Log
    @Insert("INSERT INTO t_notification_log (user_id, content, channel, status, source_event_type) " +
            "VALUES (#{userId}, #{content}, #{channel}, #{status}, #{sourceEventType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNotificationLog(NotificationLog notificationLog);

    @Select("SELECT * FROM t_notification_log WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<NotificationLog> selectNotificationsByUserId(Long userId);

    @Update("UPDATE t_notification_log SET status = #{status} WHERE id = #{id}")
    int updateNotificationStatus(@Param("id") Long id, @Param("status") String status);

    // Notification Preference
    @Insert("INSERT INTO t_notification_preference (user_id, event_type, channel, alert_type, is_enabled) " +
            "VALUES (#{userId}, #{eventType}, #{channel}, #{alertType}, #{isEnabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertPreference(NotificationPreference preference);

    @Select("SELECT * FROM t_notification_preference WHERE user_id = #{userId}")
    List<NotificationPreference> selectPreferencesByUserId(Long userId);

    @Select("SELECT * FROM t_notification_preference WHERE user_id = #{userId} AND event_type = #{eventType}")
    List<NotificationPreference> selectPreferencesByUserAndEvent(@Param("userId") Long userId,
                                                                 @Param("eventType") String eventType);

    @Update("UPDATE t_notification_preference SET alert_type = #{alertType}, is_enabled = #{isEnabled} " +
            "WHERE user_id = #{userId} AND event_type = #{eventType} AND channel = #{channel}")
    int updatePreference(NotificationPreference preference);

    // DND Settings
    @Insert("INSERT INTO t_notification_dnd (user_id, is_enabled, start_time, end_time) " +
            "VALUES (#{userId}, #{isEnabled}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDndSetting(NotificationDnd dnd);

    @Select("SELECT * FROM t_notification_dnd WHERE user_id = #{userId}")
    NotificationDnd selectDndByUserId(Long userId);

    @Update("UPDATE t_notification_dnd SET is_enabled = #{isEnabled}, start_time = #{startTime}, end_time = #{endTime} " +
            "WHERE user_id = #{userId}")
    int updateDndSetting(NotificationDnd dnd);

    // 添加删除通知的方法
    @Delete("DELETE FROM t_notification_log WHERE id = #{notificationId}")
    int deleteNotification(@Param("notificationId") Long notificationId);

    // 添加根据ID查询通知的方法（用于删除前检查）
    @Select("SELECT * FROM t_notification_log WHERE id = #{notificationId}")
    NotificationLog selectNotificationById(@Param("notificationId") Long notificationId);


    // 偏好设置相关方法




    @Select("SELECT * FROM t_notification_preference WHERE id = #{preferenceId}")
    NotificationPreference selectPreferenceById(@Param("preferenceId") Long preferenceId);



    // 新增：删除单个偏好设置
    @Delete("DELETE FROM t_notification_preference WHERE id = #{preferenceId}")
    int deletePreference(@Param("preferenceId") Long preferenceId);

    // 新增：删除用户的所有偏好设置
    @Delete("DELETE FROM t_notification_preference WHERE user_id = #{userId}")
    int deletePreferencesByUserId(@Param("userId") Long userId);
}