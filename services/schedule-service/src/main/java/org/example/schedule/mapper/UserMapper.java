package org.example.schedule.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 * 用于查询系统中的所有用户ID
 */
@Mapper
public interface UserMapper {
    
    /**
     * 查询所有用户ID
     * @return 用户ID列表
     */
    @Select("SELECT DISTINCT user_id FROM t_schedule WHERE user_id IS NOT NULL")
    List<Long> selectAllUserIds();
}