package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleType;

import java.util.List;

/**
 * 日程类型Mapper接口
 * 负责t_schedule_type表的数据操作
 */
@Mapper
public interface ScheduleTypeMapper {
    
    /**
     * 根据ID查询日程类型
     * @param id 类型ID
     * @return 日程类型实体
     */
    @Select("SELECT * FROM t_schedule_type WHERE id = #{id}")
    ScheduleType selectById(Long id);
    
    /**
     * 根据用户ID查询日程类型（包括系统预设类型）
     * @param userId 用户ID
     * @return 日程类型列表
     */
    @Select("SELECT * FROM t_schedule_type WHERE user_id = #{userId} OR user_id IS NULL")
    List<ScheduleType> selectByUserId(Long userId);
    
    /**
     * 查询所有日程类型
     * @return 日程类型列表
     */
    @Select("SELECT * FROM t_schedule_type")
    List<ScheduleType> selectAll();
    
    /**
     * 插入新的日程类型
     * @param scheduleType 日程类型实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_schedule_type(user_id, type_name, color_hex) VALUES(#{userId}, #{typeName}, #{colorHex})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduleType scheduleType);
    
    /**
     * 更新日程类型
     * @param scheduleType 日程类型实体
     * @return 影响的行数
     */
    @Update("UPDATE t_schedule_type SET user_id = #{userId}, type_name = #{typeName}, color_hex = #{colorHex} WHERE id = #{id}")
    int update(ScheduleType scheduleType);
    
    /**
     * 根据ID删除日程类型
     * @param id 类型ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_type WHERE id = #{id}")
    int deleteById(Long id);
    
    /**
     * 根据类型名称查询日程类型
     * @param typeName 类型名称
     * @return 日程类型实体
     */
    @Select("SELECT * FROM t_schedule_type WHERE type_name = #{typeName}")
    ScheduleType selectByTypeName(String typeName);
}