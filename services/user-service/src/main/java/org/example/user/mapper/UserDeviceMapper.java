package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDeviceMapper {
    int deleteByUserId(@Param("userId") String userId);
    int insert(@Param("userId") String userId, @Param("deviceId") String deviceId);
    List<String> listDeviceIds(@Param("userId") String userId);
}