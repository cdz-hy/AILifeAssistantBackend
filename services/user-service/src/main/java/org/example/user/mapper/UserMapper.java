package org.example.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.user.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User findById(@Param("id") String id);
    User findByUsername(@Param("username") String username);
    int insert(User user);
    int updateProfile(@Param("id") String id, @Param("nickname") String nickname, @Param("avatarUrl") String avatarUrl);
    int updateAuthorizations(@Param("id") String id, @Param("consentHealthAnalysis") Boolean consentHealthAnalysis, @Param("consentFinanceAnalysis") Boolean consentFinanceAnalysis);
    int updatePersona(@Param("id") String id, @Param("personaJson") String personaJson);
    List<String> listDeviceIds(@Param("userId") String userId);
}