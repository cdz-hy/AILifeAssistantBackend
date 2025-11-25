//package org.example.dietrecord.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 允许前端地址
//        config.addAllowedOrigin("http://localhost:8080");
//
//        // 允许所有请求头
//        config.addAllowedHeader("*");
//
//        // 允许所有HTTP方法
//        config.addAllowedMethod("*");
//
//        // 允许携带凭证
//        config.setAllowCredentials(true);
//
//        // 预检请求的缓存时间（秒）
//        config.setMaxAge(3600L);
//
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
//
