package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleReminder;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleReminderMapper {
    
    @Select("SELECT * FROM t_schedule_reminder WHERE id = #{id}")
    ScheduleReminder selectById(Long id);
    
    @Select("SELECT * FROM t_schedule_reminder WHERE schedule_id = #{scheduleId}")
    List<ScheduleReminder> selectByScheduleId(Long scheduleId);
    
    @Select("SELECT * FROM t_schedule_reminder WHERE remind_at <= #{currentTime} AND status = 'pending'")
    List<ScheduleReminder> selectPendingReminders(LocalDateTime currentTime);
    
    @Insert("INSERT INTO t_schedule_reminder(schedule_id, remind_at, status) VALUES(#{scheduleId}, #{remindAt}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduleReminder scheduleReminder);
    
    @Update("UPDATE t_schedule_reminder SET schedule_id = #{scheduleId}, remind_at = #{remindAt}, status = #{status} WHERE id = #{id}")
    int update(ScheduleReminder scheduleReminder);
    
    @Delete("DELETE FROM t_schedule_reminder WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM t_schedule_reminder WHERE schedule_id = #{scheduleId}")
    int deleteByScheduleId(Long scheduleId);
}