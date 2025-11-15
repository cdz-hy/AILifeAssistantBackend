package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleTag;

import java.util.List;

/**
 * 日程标签映射Mapper接口
 * 负责t_schedule_tag_map表的数据操作
 */
@Mapper
public interface ScheduleTagMapMapper {
    
    /**
     * 根据日程ID查询关联的标签
     * @param scheduleId 日程ID
     * @return 标签列表
     */
    @Select("SELECT st.* FROM t_schedule_tag st " +
            "JOIN t_schedule_tag_map stm ON st.id = stm.tag_id " +
            "WHERE stm.schedule_id = #{scheduleId}")
    List<ScheduleTag> selectTagsByScheduleId(Long scheduleId);
    
    /**
     * 为日程添加标签
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    @Insert("INSERT INTO t_schedule_tag_map(schedule_id, tag_id) VALUES(#{scheduleId}, #{tagId})")
    int insert(@Param("scheduleId") Long scheduleId, @Param("tagId") Long tagId);
    
    /**
     * 删除日程的标签关联
     * @param scheduleId 日程ID
     * @param tagId 标签ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_tag_map WHERE schedule_id = #{scheduleId} AND tag_id = #{tagId}")
    int delete(@Param("scheduleId") Long scheduleId, @Param("tagId") Long tagId);
    
    /**
     * 删除日程的所有标签关联
     * @param scheduleId 日程ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_tag_map WHERE schedule_id = #{scheduleId}")
    int deleteByScheduleId(Long scheduleId);
    
    /**
     * 删除标签的所有关联
     * @param tagId 标签ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM t_schedule_tag_map WHERE tag_id = #{tagId}")
    int deleteByTagId(Long tagId);
}