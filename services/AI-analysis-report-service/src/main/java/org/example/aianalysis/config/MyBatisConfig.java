package org.example.aianalysis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan("org.example.aianalysis.mapper")
public class MyBatisConfig {
}