package org.example.finance.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.example.finance.mapper")
public class MyBatisConfig {
    // MyBatis配置
}