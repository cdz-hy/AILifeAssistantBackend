package org.example.schedule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson配置类
 * 用于配置时间格式的序列化
 */
@Configuration
public class JacksonConfig {
    
    /**
     * 配置ObjectMapper，设置LocalDateTime的序列化格式
     * @return 配置后的ObjectMapper实例
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 设置LocalDateTime的序列化格式
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(javaTimeModule);
        return mapper;
    }
}