package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.UserCore;

@Mapper
public interface UserCoreMapper {
    UserCore findById(@Param("id") Long id);
    UserCore findByUsername(@Param("username") String username);
    int insert(UserCore userCore);
    int updateEmail(@Param("id") Long id, @Param("email") String email);
}