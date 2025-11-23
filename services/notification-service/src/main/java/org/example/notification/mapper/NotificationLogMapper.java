package org.example.notification.mapper;

import org.apache.ibatis.annotations.*;
import org.example.notification.entity.NotificationLog;

/**
 * 通知日志Mapper接口
 * 负责t_notification_log表的数据操作
 */
@Mapper
public interface NotificationLogMapper {

    /**
     * 插入新的通知日志
     * @param notificationLog 通知日志实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_notification_log(user_id, content, channel, status, source_event_type, created_at) " +
            "VALUES(#{userId}, #{content}, #{channel}, #{status}, #{sourceEventType}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(NotificationLog notificationLog);
}