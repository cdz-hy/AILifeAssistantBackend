package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.ScheduleAISuggestion;

import java.util.List;

@Mapper
public interface ScheduleAISuggestionMapper {
    
    @Select("SELECT * FROM t_schedule_ai_suggestion WHERE id = #{id}")
    ScheduleAISuggestion selectById(Long id);
    
    @Select("SELECT * FROM t_schedule_ai_suggestion WHERE schedule_id = #{scheduleId}")
    List<ScheduleAISuggestion> selectByScheduleId(Long scheduleId);
    
    @Insert("INSERT INTO t_schedule_ai_suggestion(schedule_id, suggestion_type, suggestion_content, is_accepted) " +
            "VALUES(#{scheduleId}, #{suggestionType}, #{suggestionContent}, #{isAccepted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduleAISuggestion scheduleAISuggestion);
    
    @Update("UPDATE t_schedule_ai_suggestion SET schedule_id = #{scheduleId}, suggestion_type = #{suggestionType}, " +
            "suggestion_content = #{suggestionContent}, is_accepted = #{isAccepted} WHERE id = #{id}")
    int update(ScheduleAISuggestion scheduleAISuggestion);
    
    @Delete("DELETE FROM t_schedule_ai_suggestion WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM t_schedule_ai_suggestion WHERE schedule_id = #{scheduleId}")
    int deleteByScheduleId(Long scheduleId);
}