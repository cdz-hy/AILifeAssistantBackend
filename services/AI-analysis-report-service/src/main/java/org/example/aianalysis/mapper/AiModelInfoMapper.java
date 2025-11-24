package org.example.aianalysis.mapper;

import org.apache.ibatis.annotations.*;
import org.example.aianalysis.entity.AiModelInfo;

import java.util.List;

@Mapper
public interface AiModelInfoMapper {
    
    @Select("SELECT * FROM t_ai_model_info WHERE id = #{id}")
    AiModelInfo selectById(Long id);
    
    @Select("SELECT * FROM t_ai_model_info WHERE model_name = #{modelName} AND version = #{version}")
    AiModelInfo selectByModelNameAndVersion(@Param("modelName") String modelName, @Param("version") String version);
    
    @Select("SELECT * FROM t_ai_model_info")
    List<AiModelInfo> selectAll();
    
    @Insert("INSERT INTO t_ai_model_info(model_name, version, description) VALUES(#{modelName}, #{version}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AiModelInfo aiModelInfo);
    
    @Update("UPDATE t_ai_model_info SET model_name = #{modelName}, version = #{version}, description = #{description} WHERE id = #{id}")
    int update(AiModelInfo aiModelInfo);
    
    @Delete("DELETE FROM t_ai_model_info WHERE id = #{id}")
    int deleteById(Long id);
}