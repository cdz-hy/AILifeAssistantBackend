package org.example.schedule.controller;

import org.example.schedule.entity.WeatherCache;
import org.example.schedule.service.ScheduleService;
import org.example.schedule.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private WeatherService weatherService;

    /**
     * 根据城市编码获取天气信息
     * @param cityCode 城市编码
     * @return 天气缓存实体
     */
    @GetMapping("/city/{cityCode}")
    public WeatherCache getWeatherByCityCode(@PathVariable String cityCode, @RequestParam(value = "extensions", required = false) String extensions) {
        return weatherService.getWeatherByCityCode(cityCode, extensions);
    }

    @GetMapping("/cache/{cacheKey}")
    public WeatherCache getWeatherCache(@PathVariable String cacheKey) {
        return scheduleService.getValidWeatherCache(cacheKey);
    }

    @PostMapping("/cache")
    public WeatherCache saveWeatherCache(@RequestBody WeatherCache weatherCache) {
        return scheduleService.saveWeatherCache(weatherCache);
    }

    @DeleteMapping("/cache/{cacheKey}")
    public void deleteWeatherCache(@PathVariable String cacheKey) {
        scheduleService.deleteWeatherCache(cacheKey);
    }

    @DeleteMapping("/cache/clean")
    public void cleanExpiredWeatherCache() {
        scheduleService.cleanExpiredWeatherCache();
    }
}