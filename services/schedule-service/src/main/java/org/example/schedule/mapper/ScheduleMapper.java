package org.example.schedule.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    Schedule selectById(Long id);
    
    /**
     * 根据用户ID查询所有日程
     * @param userId 用户ID
     * @return 日程列表
     */
    List<Schedule> selectByUserId(Long userId);
    
    /**
     * 根据用户ID和日期范围查询日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    List<Schedule> selectByUserIdAndDateRange(@Param("userId") Long userId, 
                                              @Param("startTime") LocalDateTime startTime, 
                                              @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据用户ID和日期范围查询非重复日程
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日程列表
     */
    List<Schedule> selectNonRecurringByUserIdAndDateRange(@Param("userId") Long userId,
                                                          @Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据用户ID查询在指定时间之前开始的重复日程
     * @param userId 用户ID
     * @param endTime 结束时间
     * @return 日程列表
     */
    List<Schedule> selectRecurringStartedBefore(@Param("userId") Long userId,
                                                @Param("endTime") LocalDateTime endTime);
    
    /**
     * 插入新的日程
     * @param schedule 日程实体
     * @return 影响的行数
     */
    int insert(Schedule schedule);
    
    /**
     * 更新日程
     * @param schedule 日程实体
     * @return 影响的行数
     */
    int update(Schedule schedule);
    
    /**
     * 根据ID删除日程
     * @param id 日程ID
     * @return 影响的行数
     */
    int deleteById(Long id);
    
    /**
     * 根据类型ID查询日程
     * @param typeId 类型ID
     * @return 日程列表
     */
    List<Schedule> selectByTypeId(Long typeId);
    
    /**
     * 根据状态查询日程
     * @param userId 用户ID
     * @param status 状态
     * @return 日程列表
     */
    List<Schedule> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    /**
     * 根据紧急重要性查询日程
     * @param userId 用户ID
     * @param isUrgent 是否紧急
     * @param isImportant 是否重要
     * @return 日程列表
     */
    List<Schedule> selectByUrgencyAndImportance(@Param("userId") Long userId, 
                                               @Param("isUrgent") Boolean isUrgent, 
                                               @Param("isImportant") Boolean isImportant);
    
    /**
     * 多条件组合查询日程
     * @param userId 用户ID
     * @param typeId 类型ID（可选）
     * @param status 状态（可选）
     * @param isUrgent 是否紧急（可选）
     * @param isImportant 是否重要（可选）
     * @param startTime 开始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 日程列表
     */
    List<Schedule> selectByMultipleConditions(@Param("userId") Long userId, 
                                             @Param("typeId") Long typeId, 
                                             @Param("status") String status, 
                                             @Param("isUrgent") Boolean isUrgent, 
                                             @Param("isImportant") Boolean isImportant, 
                                             @Param("startTime") LocalDateTime startTime, 
                                             @Param("endTime") LocalDateTime endTime);
}