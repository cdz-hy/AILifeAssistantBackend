package org.example.schedule.config;

import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * 配置Mapper扫描路径和类型处理器
 */
@Configuration
@MapperScan("org.example.schedule.mapper")
public class MyBatisConfig {
}