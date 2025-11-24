package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.UserProfile;

@Mapper
public interface UserProfileMapper {
    UserProfile findByUserId(@Param("userId") Long userId);
    int insert(UserProfile profile);
    int update(@Param("userId") Long userId, @Param("nickname") String nickname, @Param("avatarUrl") String avatarUrl);
}