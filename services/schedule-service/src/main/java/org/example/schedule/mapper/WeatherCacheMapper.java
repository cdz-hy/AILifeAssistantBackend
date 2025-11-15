package org.example.schedule.mapper;

import org.apache.ibatis.annotations.*;
import org.example.schedule.entity.WeatherCache;

import java.time.LocalDateTime;

@Mapper
public interface WeatherCacheMapper {
    
    @Select("SELECT * FROM t_weather_cache WHERE cache_key = #{cacheKey}")
    WeatherCache selectById(String cacheKey);
    
    @Select("SELECT * FROM t_weather_cache WHERE cache_key = #{cacheKey} AND expires_at > #{currentTime}")
    WeatherCache selectValidById(@Param("cacheKey") String cacheKey, @Param("currentTime") LocalDateTime currentTime);
    
    @Insert("INSERT INTO t_weather_cache(cache_key, data_json, expires_at) VALUES(#{cacheKey}, #{dataJson}, #{expiresAt})")
    int insert(WeatherCache weatherCache);
    
    @Update("UPDATE t_weather_cache SET data_json = #{dataJson}, expires_at = #{expiresAt} WHERE cache_key = #{cacheKey}")
    int update(WeatherCache weatherCache);
    
    @Delete("DELETE FROM t_weather_cache WHERE cache_key = #{cacheKey}")
    int deleteById(String cacheKey);
    
    @Delete("DELETE FROM t_weather_cache WHERE expires_at < #{currentTime}")
    int deleteExpired(LocalDateTime currentTime);
}