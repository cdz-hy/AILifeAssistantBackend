//package org.example.study.config;
//
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
//        // 1. 创建CORS配置对象
//        CorsConfiguration config = new CorsConfiguration();
//        // 允许前端域名访问（这里填写你的前端地址）
//        config.addAllowedOrigin("http://localhost:8080");
//        // 允许跨域请求携带cookie
//        config.setAllowCredentials(true);
//        // 允许的请求方法（GET, POST, PUT, DELETE等）
//        config.addAllowedMethod("*");
//        // 允许的请求头
//        config.addAllowedHeader("*");
//        // 暴露的响应头（前端需要获取的头信息）
//        config.addExposedHeader("*");
//
//        // 2. 配置映射路径
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // 对所有接口生效
//        source.registerCorsConfiguration("/**", config);
//
//        // 3. 返回CORS过滤器
//        return new CorsFilter(source);
//    }
//}