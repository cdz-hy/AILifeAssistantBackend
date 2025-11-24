package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoundDeviceMapper {
    int deleteByUserId(@Param("userId") Long userId);
    int insert(@Param("userId") Long userId, @Param("deviceType") String deviceType, @Param("deviceToken") String deviceToken);
    List<String> listDeviceTokens(@Param("userId") Long userId);
}