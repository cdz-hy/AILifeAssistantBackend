package org.example.schedule.service;

import org.example.schedule.entity.WeatherCache;

/**
 * 天气服务接口
 * 负责天气相关业务逻辑
 */
public interface WeatherService {
    
    /**
     * 根据缓存键获取天气缓存
     * @param cacheKey 缓存键
     * @return 天气缓存实体
     */
    WeatherCache getWeatherCache(String cacheKey);
    
    /**
     * 根据缓存键获取有效的天气缓存
     * @param cacheKey 缓存键
     * @return 天气缓存实体
     */
    WeatherCache getValidWeatherCache(String cacheKey);
    
    /**
     * 保存天气缓存
     * @param weatherCache 天气缓存实体
     * @return 保存后的天气缓存实体
     */
    WeatherCache saveWeatherCache(WeatherCache weatherCache);
    
    /**
     * 删除天气缓存
     * @param cacheKey 缓存键
     */
    void deleteWeatherCache(String cacheKey);
    
    /**
     * 清理过期的天气缓存
     */
    void cleanExpiredWeatherCache();
}