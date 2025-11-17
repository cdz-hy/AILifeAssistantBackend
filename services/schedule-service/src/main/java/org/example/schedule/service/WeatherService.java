package org.example.schedule.service;

import org.example.schedule.entity.WeatherCache;

/**
 * 天气服务接口
 * 提供天气查询相关业务逻辑
 */
public interface WeatherService {
    
    /**
     * 根据城市编码获取天气信息
     * @param cityCode 城市编码
     * @return 天气缓存实体
     */
    WeatherCache getWeatherByCityCode(String cityCode);
    
    /**
     * 调用高德地图API获取天气信息
     * @param cityCode 城市编码
     * @return 天气数据JSON字符串
     */
    String fetchWeatherFromAmap(String cityCode);
    
    /**
     * 从Nacos配置中心获取高德地图API Key
     * @return API Key
     */
    String getAmapApiKey();
}