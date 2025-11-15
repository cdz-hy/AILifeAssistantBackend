package org.example.schedule.controller;

import org.example.schedule.entity.WeatherCache;
import org.example.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    
    @Autowired
    private ScheduleService scheduleService;
    
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