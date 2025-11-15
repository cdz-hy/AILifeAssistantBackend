package org.example.schedule.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 重复日程工具类
 * 处理重复日程的计算和生成
 */
public class RecurrenceUtils {
    
    /**
     * 根据重复规则生成日程实例
     * @param startTime 起始时间
     * @param recurrenceRule 重复规则 (iCal RRULE格式)
     * @param count 生成实例数量
     * @return 日程时间列表
     */
    public static List<LocalDateTime> generateRecurrenceInstances(LocalDateTime startTime, String recurrenceRule, int count) {
        // 简化实现，实际项目中可以使用iCal4j等库处理RRULE
        List<LocalDateTime> instances = new ArrayList<>();
        
        if (recurrenceRule == null || recurrenceRule.isEmpty()) {
            instances.add(startTime);
            return instances;
        }
        
        // 解析简单的重复规则
        if (recurrenceRule.contains("FREQ=DAILY")) {
            for (int i = 0; i < count; i++) {
                instances.add(startTime.plusDays(i));
            }
        } else if (recurrenceRule.contains("FREQ=WEEKLY")) {
            for (int i = 0; i < count; i++) {
                instances.add(startTime.plusWeeks(i));
            }
        } else if (recurrenceRule.contains("FREQ=MONTHLY")) {
            for (int i = 0; i < count; i++) {
                instances.add(startTime.plusMonths(i));
            }
        } else {
            // 默认只添加起始时间
            instances.add(startTime);
        }
        
        return instances;
    }
    
    /**
     * 检查时间是否符合重复规则
     * @param time 要检查的时间
     * @param startTime 起始时间
     * @param recurrenceRule 重复规则
     * @return 是否符合重复规则
     */
    public static boolean matchesRecurrenceRule(LocalDateTime time, LocalDateTime startTime, String recurrenceRule) {
        if (recurrenceRule == null || recurrenceRule.isEmpty()) {
            return time.equals(startTime);
        }
        
        // 简化实现，实际项目中可以使用iCal4j等库处理RRULE
        if (recurrenceRule.contains("FREQ=DAILY")) {
            return !time.isBefore(startTime) && 
                   (time.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay()) % 1 == 0;
        } else if (recurrenceRule.contains("FREQ=WEEKLY")) {
            return !time.isBefore(startTime) && 
                   (time.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay()) % 7 == 0;
        } else if (recurrenceRule.contains("FREQ=MONTHLY")) {
            return !time.isBefore(startTime) && 
                   time.getDayOfMonth() == startTime.getDayOfMonth();
        }
        
        return time.equals(startTime);
    }
}