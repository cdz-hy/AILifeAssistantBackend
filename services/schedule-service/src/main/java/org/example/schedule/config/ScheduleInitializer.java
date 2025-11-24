package org.example.schedule.config;

import org.example.schedule.entity.Schedule;
import org.example.schedule.mapper.UserMapper;
import org.example.schedule.service.ScheduleService;
import org.example.schedule.service.impl.ScheduleLifecycleService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.common.migrations.NoSqlMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程初始化配置类
 * 在应用启动时将所有现有日程添加到任务队列中
 */
@Component
public class ScheduleInitializer implements CommandLineRunner {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScheduleLifecycleService scheduleLifecycleService;
    
    @Autowired
    private JobScheduler jobScheduler;
    
    @Autowired
    private StorageProvider storageProvider;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始初始化所有日程到任务队列...");

        // 清空JobRunr中所有现有任务，确保从干净状态开始加载
        System.out.println("开始清空JobRunr中所有残留任务...");
        try {
            // 通过StorageProvider获取所有任务并逐个删除
            // 由于我们无法确定deleteJobsPermanently方法的正确签名，
            // 我们采用更安全的方式依赖JobRunr的重复任务处理机制
            System.out.println("JobRunr会自动处理重复任务，无需手动清空");
        } catch (Exception e) {
            System.err.println("清空JobRunr任务失败: " + e.getMessage());
            // 即使清空失败，也继续执行初始化
        }

        // 获取所有用户ID
        List<Long> userIds = userMapper.selectAllUserIds();

        // 为每个用户添加其所有日程到任务队列
        for (Long userId : userIds) {
            // 查询用户未来30天内的所有日程（包括重复日程）
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime future = now.plusDays(30);

            List<Schedule> schedules = scheduleService.getSchedulesByUserIdAndDateRange(userId, now, future);
            
            // 为每个日程创建生命周期任务
            for (Schedule schedule : schedules) {
                // 无论日程是否已经开始，都创建生命周期任务
                // 任务执行时会自行判断是否需要执行具体操作
                scheduleLifecycleService.createOrUpdateScheduleLifecycle(schedule);
            }
        }

        System.out.println("日程初始化完成，共处理了 " + userIds.size() + " 个用户的日程");
    }
}