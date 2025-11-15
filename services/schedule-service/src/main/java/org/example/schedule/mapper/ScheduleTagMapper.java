package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleTag;

import java.util.List;

/**
 * 日程标签Mapper接口
 * 负责t_schedule_tag表的数据操作
 */
@Mapper
public interface ScheduleTagMapper {
    
    /**
     * 根据ID查询日程标签
     * @param id 标签ID
     * @return 日程标签实体
     */
    @Select("SELECT * FROM t_schedule_tag WHERE id = #{id}")
    ScheduleTag selectById(Long id);
    
    /**
     * 查询所有日程标签
     * @return 日程标签列表
     */
    @Select("SELECT * FROM t_schedule_tag")
    List<ScheduleTag> selectAll();
    
    /**
     * 根据标签名称查询日程标签
     * @param tagName 标签名称
     * @return 日程标签实体
     */
    @Select("SELECT * FROM t_schedule_tag WHERE tag_name = #{tagName}")
    ScheduleTag selectByTagName(String tagName);
    
    /**
     * 插入新的日程标签
     * @param scheduleTag 日程标签实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_schedule_tag(tag_name, created_by) VALUES(#{tagName}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduleTag scheduleTag);
    
    /**
     * 更新日程标签
     * @param scheduleTag 日程标签实体
     * @return 影响的行数
     */
    @Update("UPDATE t_schedule_tag SET tag_name = #{tagName}, created_by = #{createdBy} WHERE id = #{id}")
    int update(ScheduleTag scheduleTag);
    
    /**
     * 根据ID删除日程标签
     * @param id 标签ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_tag WHERE id = #{id}")
    int deleteById(Long id);
}