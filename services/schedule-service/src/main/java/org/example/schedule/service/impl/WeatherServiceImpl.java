package org.example.schedule.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.schedule.entity.WeatherCache;
import org.example.schedule.mapper.WeatherCacheMapper;
import org.example.schedule.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 天气服务实现类
 * 实现天气查询相关业务逻辑
 */
@Service
@RefreshScope
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherCacheMapper weatherCacheMapper;

    @Value("${Key:}")
    private String amapApiKey;

    @Value("${Weather.extensions:base}")
    private String extensions;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WeatherCache getWeatherByCityCode(String cityCode) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String cacheKey = cityCode + "-" + extensions + "-" + dateStr;

        // 检查缓存中是否存在该城市的天气数据
        WeatherCache weatherCache = weatherCacheMapper.selectById(cacheKey);

        if (weatherCache != null) {
            // 检查是否过期
            if (weatherCache.getExpiresAt().isAfter(LocalDateTime.now())) {
                // 未过期，直接返回缓存数据
                return weatherCache;
            } else {
                // 已过期，重新获取天气数据并更新缓存
                String weatherData = fetchWeatherFromAmap(cityCode);
                if (weatherData != null) {
                    weatherCache.setDataJson(weatherData);
                    weatherCache.setExpiresAt(LocalDateTime.now().plusHours(1));
                    weatherCacheMapper.update(weatherCache);
                    return weatherCache;
                }
            }
        } else {
            // 缓存中不存在，获取天气数据并新增缓存记录
            String weatherData = fetchWeatherFromAmap(cityCode);
            if (weatherData != null) {
                weatherCache = new WeatherCache();
                weatherCache.setCacheKey(cacheKey);
                weatherCache.setDataJson(weatherData);
                weatherCache.setExpiresAt(LocalDateTime.now().plusHours(1));
                weatherCacheMapper.insert(weatherCache);
                return weatherCache;
            }
        }

        return null;
    }

    @Override
    public WeatherCache getWeatherByCityCode(String cityCode, String ext) {
        String extUsed = (ext != null && !ext.isEmpty()) ? ext : this.extensions;
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String cacheKey = cityCode + "-" + extUsed + "-" + dateStr;
        WeatherCache weatherCache = weatherCacheMapper.selectById(cacheKey);
        if (weatherCache != null) {
            if (weatherCache.getExpiresAt().isAfter(LocalDateTime.now())) {
                return weatherCache;
            } else {
                String weatherData = fetchWeatherFromAmap(cityCode, extUsed);
                if (weatherData != null) {
                    weatherCache.setDataJson(weatherData);
                    weatherCache.setExpiresAt(LocalDateTime.now().plusHours(1));
                    weatherCacheMapper.update(weatherCache);
                    return weatherCache;
                }
            }
        } else {
            String weatherData = fetchWeatherFromAmap(cityCode, extUsed);
            if (weatherData != null) {
                weatherCache = new WeatherCache();
                weatherCache.setCacheKey(cacheKey);
                weatherCache.setDataJson(weatherData);
                weatherCache.setExpiresAt(LocalDateTime.now().plusHours(1));
                weatherCacheMapper.insert(weatherCache);
                return weatherCache;
            }
        }
        return null;
    }

    @Override
    public String fetchWeatherFromAmap(String cityCode) {
        if (amapApiKey == null || amapApiKey.isEmpty()) {
            System.err.println("高德地图API Key未配置");
            return null;
        }

        try {
            String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + amapApiKey + "&city=" + cityCode + "&extensions=" + extensions + "&output=json";

            String response = restTemplate.getForObject(url, String.class);

            if (response != null) {
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode statusNode = jsonNode.get("status");

                if (statusNode != null && "1".equals(statusNode.asText())) {
                    return response;
                } else {
                    System.err.println("高德地图API请求失败: " + response);
                }
            }
        } catch (Exception e) {
            System.err.println("调用高德地图API异常: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String fetchWeatherFromAmap(String cityCode, String ext) {
        if (amapApiKey == null || amapApiKey.isEmpty()) {
            System.err.println("高德地图API Key未配置");
            return null;
        }
        try {
            String extUsed = (ext != null && !ext.isEmpty()) ? ext : this.extensions;
            String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + amapApiKey + "&city=" + cityCode + "&extensions=" + extUsed + "&output=json";
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                JsonNode jsonNode = objectMapper.readTree(response);
                JsonNode statusNode = jsonNode.get("status");
                if (statusNode != null && "1".equals(statusNode.asText())) {
                    return response;
                } else {
                    System.err.println("高德地图API请求失败: " + response);
                }
            }
        } catch (Exception e) {
            System.err.println("调用高德地图API异常: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAmapApiKey() {
        return amapApiKey;
    }
}