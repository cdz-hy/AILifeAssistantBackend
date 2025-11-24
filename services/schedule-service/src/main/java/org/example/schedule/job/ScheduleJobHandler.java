package org.example.schedule.job;

import org.example.schedule.entity.Schedule;
import org.example.schedule.mapper.ScheduleMapper;
import org.example.schedule.service.NotificationServiceClient;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 日程任务处理器
 * 负责处理JobRunr调度的任务，包括提醒、开始和结束等事件
 */
@Service
public class ScheduleJobHandler {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    /**
     * 处理日程提醒任务
     * @param scheduleId 日程ID
     * @param expectedRemindTime 预期提醒时间
     */
    @Job(name = "发送日程提醒")
    public void executeRemind(Long scheduleId, LocalDateTime expectedRemindTime) {
        System.out.println("JobRunr: 开始执行提醒任务，日程ID: " + scheduleId);

        // 1. 【僵尸检查】
        // 从数据库查出最新的日程
        Schedule currentSchedule = scheduleMapper.selectById(scheduleId);

        // 情况1: 日程被删了 -> 废弃
        if (currentSchedule == null) return;

        // 情况2: 用户改期了 -> 废弃
        // 逻辑：如果数据库里的最新提醒时间，跟这个任务里存的 expectedRemindTime 不一致，说明这个任务是旧的
        LocalDateTime currentDbRemindTime = currentSchedule.getStartTime().minusMinutes(15); // 提前15分钟提醒
        if (!currentDbRemindTime.isEqual(expectedRemindTime)) {
            System.out.println("检测到过期提醒任务，已丢弃。");
            return;
        }
        
        // 情况3: 提醒时间已过 -> 废弃
        if (expectedRemindTime.isBefore(LocalDateTime.now())) {
            System.out.println("提醒时间已过，无需发送提醒。");
            return;
        }

        // 2. 执行业务：调用通知服务
        String content = "您的日程即将开始：" + currentSchedule.getTitle();
        notificationServiceClient.sendNotification(
                currentSchedule.getUserId(),
                content,
                "schedule_reminder",
                "push"
        );
    }

    /**
     * 处理日程开始任务
     * @param scheduleId 日程ID
     * @param expectedStartTime 预期开始时间
     */
    @Job(name = "更新日程状态-进行中")
    public void executeStart(Long scheduleId, LocalDateTime expectedStartTime) {
        // 1. 【僵尸检查】
        Schedule currentSchedule = scheduleMapper.selectById(scheduleId);
        if (currentSchedule == null) return;

        // 如果数据库里的开始时间 != 任务里的预期时间，说明被改期了，丢弃
        if (!currentSchedule.getStartTime().isEqual(expectedStartTime)) {
            System.out.println("检测到过期开始任务，已丢弃。");
            return;
        }

        // 2. 执行业务：更新状态
        if ("pending".equals(currentSchedule.getStatus())) {
            currentSchedule.setStatus("in_progress");
            scheduleMapper.update(currentSchedule);
            System.out.println("日程已自动标记为进行中");
        }
    }

    /**
     * 处理日程结束任务
     * @param scheduleId 日程ID
     * @param expectedEndTime 预期结束时间
     */
    @Job(name = "更新日程状态-已完成")
    public void executeEnd(Long scheduleId, LocalDateTime expectedEndTime) {
        // 1. 【僵尸检查】
        Schedule currentSchedule = scheduleMapper.selectById(scheduleId);
        if (currentSchedule == null) return;

        // 如果数据库里的结束时间 != 任务里的预期时间，说明被改期了，丢弃
        if (!currentSchedule.getEndTime().isEqual(expectedEndTime)) {
            System.out.println("检测到过期结束任务，已丢弃。");
            return;
        }

        // 2. 执行业务：更新状态
        currentSchedule.setStatus("completed");
        scheduleMapper.update(currentSchedule);
        System.out.println("日程已自动标记为已完成");
    }
}