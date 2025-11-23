package org.example.schedule.service.impl;

import org.example.schedule.entity.Schedule;
import org.example.schedule.job.ScheduleJobHandler;
import org.example.schedule.service.ScheduleService;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 日程生命周期管理服务
 * 负责管理日程的全生命周期，包括创建、提醒、开始和结束等事件
 */
@Service
public class ScheduleLifecycleService {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private ScheduleJobHandler scheduleJobHandler;

    /**
     * 创建或更新日程的生命周期任务
     * @param schedule 日程实体
     */
    public void createOrUpdateScheduleLifecycle(Schedule schedule) {
        // 1. 删除之前可能存在的任务（可选，JobRunr会自动处理重复任务）
        // 但为了确保一致性，我们还是显式地发布新的任务

        // 2. 【关键】发布3个延时任务
        // 注意：我们把"预期执行时间"作为参数传进去，用于后续的僵尸检查

        // 任务A：提醒（任务开始时）
        LocalDateTime remindTime = schedule.getStartTime().minusMinutes(0);
        // 即使提醒时间已过，也要调度任务，让任务执行时自己判断是否需要发送提醒
        jobScheduler.schedule(remindTime,
                () -> scheduleJobHandler.executeRemind(schedule.getId(), remindTime));

        // 任务B：开始 (变更状态为 in_progress)
        jobScheduler.schedule(schedule.getStartTime(),
                () -> scheduleJobHandler.executeStart(schedule.getId(), schedule.getStartTime()));

        // 任务C：结束 (变更状态为 completed)
        jobScheduler.schedule(schedule.getEndTime(),
                () -> scheduleJobHandler.executeEnd(schedule.getId(), schedule.getEndTime()));

        System.out.println("日程 " + schedule.getId() + " 的生命周期任务已预约成功！");
    }
}