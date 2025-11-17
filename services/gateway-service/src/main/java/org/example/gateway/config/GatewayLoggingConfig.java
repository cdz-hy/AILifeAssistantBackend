package org.example.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * 网关日志配置类
 * 用于记录请求和响应的详细信息
 */
@Configuration
public class GatewayLoggingConfig {
    
    private static final Logger log = LoggerFactory.getLogger(GatewayLoggingConfig.class);
    
    /**
     * 全局过滤器，用于记录请求信息
     * @return GlobalFilter
     */
    @Bean
    @Order(-1)
    public GlobalFilter requestLoggingFilter() {
        return (exchange, chain) -> {
            String requestId = exchange.getRequest().getId();
            String method = exchange.getRequest().getMethod().name();
            String path = exchange.getRequest().getURI().getPath();
            
            // 记录请求信息
            log.info("收到请求 - ID: {}, 方法: {}, 路径: {}", requestId, method, path);
            
            long startTime = System.currentTimeMillis();
            
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        long executeTime = System.currentTimeMillis() - startTime;
                        int status = exchange.getResponse().getStatusCode() != null ? 
                            exchange.getResponse().getStatusCode().value() : 0;
                        
                        // 记录响应信息
                        log.info("请求完成 - ID: {}, 状态码: {}, 执行时间: {}ms", requestId, status, executeTime);
                    }));
        };
    }
}