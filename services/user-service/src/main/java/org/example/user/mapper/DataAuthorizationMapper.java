package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.DataAuthorization;

import java.util.List;

@Mapper
public interface DataAuthorizationMapper {
    DataAuthorization findOne(@Param("userId") Long userId, @Param("dataType") String dataType);
    int upsert(@Param("userId") Long userId, @Param("dataType") String dataType, @Param("isAuthorized") Boolean isAuthorized);
    List<DataAuthorization> listByUserId(@Param("userId") Long userId);
}