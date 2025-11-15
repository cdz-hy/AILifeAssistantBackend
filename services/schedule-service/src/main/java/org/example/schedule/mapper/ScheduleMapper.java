package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日程Mapper接口
 * 负责t_schedule表的数据操作
 */
@Mapper
public interface ScheduleMapper {
    
    /**
     * 根据ID查询日程
     * @param id 日程ID
     * @return 日程实体
     */
    @Select("SELECT * FROM t_schedule WHERE id = #{id}")
    Schedule selectById(Long id);
    
    /**
     * 根据用户ID查询所有日程
     * @param userId 用户ID
     * @return 日程列表
     */
    @Select("SELECT * FROM t_schedule WHERE user_id = #{userId} ORDER BY start_time")
    List<Schedule> selectByUserId(Long userId);
    
    /**
     * 根据用户ID和日期范围查询日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    @Select("SELECT * FROM t_schedule WHERE user_id = #{userId} AND start_time >= #{startTime} AND end_time <= #{endTime} ORDER BY start_time")
    List<Schedule> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                              @Param("startTime") LocalDateTime startTime, 
                                              @Param("endTime") LocalDateTime endTime);
    
    /**
     * 插入新的日程
     * @param schedule 日程实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_schedule(user_id, title, description, start_time, end_time, type_id, is_urgent, is_important, recurrence_rule, status, created_at, updated_at) " +
            "VALUES(#{userId}, #{title}, #{description}, #{startTime}, #{endTime}, #{typeId}, #{isUrgent}, #{isImportant}, #{recurrenceRule}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Schedule schedule);
    
    /**
     * 更新日程
     * @param schedule 日程实体
     * @return 影响的行数
     */
    @Update("UPDATE t_schedule SET title = #{title}, description = #{description}, start_time = #{startTime}, end_time = #{endTime}, " +
            "type_id = #{typeId}, is_urgent = #{isUrgent}, is_important = #{isImportant}, recurrence_rule = #{recurrenceRule}, " +
            "status = #{status}, updated_at = #{updatedAt} WHERE id = #{id}")
    int update(Schedule schedule);
    
    /**
     * 根据ID删除日程
     * @param id 日程ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据类型ID查询日程
     * @param typeId 类型ID
     * @return 日程列表
     */
    @Select("SELECT * FROM t_schedule WHERE type_id = #{typeId}")
    List<Schedule> selectByTypeId(Long typeId);
    
    /**
     * 根据状态查询日程
     * @param userId 用户ID
     * @param status 状态
     * @return 日程列表
     */
    @Select("SELECT * FROM t_schedule WHERE user_id = #{userId} AND status = #{status} ORDER BY start_time")
    List<Schedule> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    /**
     * 根据紧急重要性查询日程
     * @param userId 用户ID
     * @param isUrgent 是否紧急
     * @param isImportant 是否重要
     * @return 日程列表
     */
    @Select("SELECT * FROM t_schedule WHERE user_id = #{userId} AND is_urgent = #{isUrgent} AND is_important = #{isImportant} ORDER BY start_time")
    List<Schedule> selectByUrgencyAndImportance(@Param("userId") Long userId, 
                                               @Param("isUrgent") Boolean isUrgent, 
                                               @Param("isImportant") Boolean isImportant);
}