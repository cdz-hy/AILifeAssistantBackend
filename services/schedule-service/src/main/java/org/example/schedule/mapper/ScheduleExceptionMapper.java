package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleException;

import java.util.List;

/**
 * 日程例外Mapper接口
 * 负责t_schedule_exception表的数据操作
 */
@Mapper
public interface ScheduleExceptionMapper {
    
    /**
     * 根据ID查询日程例外
     * @param id 例外ID
     * @return 日程例外实体
     */
    @Select("SELECT * FROM t_schedule_exception WHERE id = #{id}")
    ScheduleException selectById(Long id);
    
    /**
     * 根据日程ID查询所有例外
     * @param scheduleId 日程ID
     * @return 日程例外列表
     */
    @Select("SELECT * FROM t_schedule_exception WHERE schedule_id = #{scheduleId}")
    List<ScheduleException> selectByScheduleId(Long scheduleId);
    
    /**
     * 插入新的日程例外
     * @param scheduleException 日程例外实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_schedule_exception(schedule_id, original_start_time, is_cancelled, new_title, new_start_time, new_end_time) " +
            "VALUES(#{scheduleId}, #{originalStartTime}, #{isCancelled}, #{newTitle}, #{newStartTime}, #{newEndTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduleException scheduleException);
    
    /**
     * 更新日程例外
     * @param scheduleException 日程例外实体
     * @return 影响的行数
     */
    @Update("UPDATE t_schedule_exception SET schedule_id = #{scheduleId}, original_start_time = #{originalStartTime}, " +
            "is_cancelled = #{isCancelled}, new_title = #{newTitle}, new_start_time = #{newStartTime}, " +
            "new_end_time = #{newEndTime} WHERE id = #{id}")
    int update(ScheduleException scheduleException);
    
    /**
     * 根据ID删除日程例外
     * @param id 例外ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_exception WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据日程ID删除所有例外
     * @param scheduleId 日程ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_exception WHERE schedule_id = #{scheduleId}")
    int deleteByScheduleId(Long scheduleId);
}