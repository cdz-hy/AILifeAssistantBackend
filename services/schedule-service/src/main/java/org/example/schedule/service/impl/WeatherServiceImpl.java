package org.example.schedule.service.impl;

import org.example.schedule.entity.WeatherCache;
import org.example.schedule.mapper.WeatherCacheMapper;
import org.example.schedule.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 天气服务实现类
 * 实现天气相关业务逻辑
 */
@Service
public class WeatherServiceImpl implements WeatherService {
    
    @Autowired
    private WeatherCacheMapper weatherCacheMapper;
    
    @Override
    public WeatherCache getWeatherCache(String cacheKey) {
        return weatherCacheMapper.selectById(cacheKey);
    }
    
    @Override
    public WeatherCache getValidWeatherCache(String cacheKey) {
        return weatherCacheMapper.selectValidById(cacheKey, LocalDateTime.now());
    }
    
    @Transactional
    @Override
    public WeatherCache saveWeatherCache(WeatherCache weatherCache) {
        WeatherCache existing = weatherCacheMapper.selectById(weatherCache.getCacheKey());
        if (existing != null) {
            weatherCacheMapper.update(weatherCache);
        } else {
            weatherCacheMapper.insert(weatherCache);
        }
        return weatherCache;
    }
    
    @Transactional
    @Override
    public void deleteWeatherCache(String cacheKey) {
        weatherCacheMapper.deleteById(cacheKey);
    }
    
    @Transactional
    @Override
    public void cleanExpiredWeatherCache() {
        weatherCacheMapper.deleteExpired(LocalDateTime.now());
    }
}