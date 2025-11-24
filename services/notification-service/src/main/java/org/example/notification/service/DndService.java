package org.example.notification.service;


import org.example.notification.dto.DndRequest;
import org.example.notification.entity.NotificationDnd;
import org.example.notification.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DndService {

    @Autowired
    private NotificationMapper notificationMapper;

    public void setDndSettings(Long userId, DndRequest request) {
        NotificationDnd dnd = notificationMapper.selectDndByUserId(userId);

        if (dnd == null) {
            // 新增记录
            dnd = new NotificationDnd();
            dnd.setUserId(userId);
            dnd.setIsEnabled(request.getIsEnabled());
            dnd.setStartTime(request.getStartTime());
            dnd.setEndTime(request.getEndTime());
            notificationMapper.insertDndSetting(dnd);
        } else {
            // 更新记录
            dnd.setIsEnabled(request.getIsEnabled());
            dnd.setStartTime(request.getStartTime());
            dnd.setEndTime(request.getEndTime());
            notificationMapper.updateDndSetting(dnd);
        }
    }

    public NotificationDnd getDndSettings(Long userId) {
        return notificationMapper.selectDndByUserId(userId);
    }
}