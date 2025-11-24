package org.example.gateway.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * 网关监控指标配置类
 * 用于收集和报告网关的性能指标
 */
@Configuration
public class GatewayMetricsConfig {
    
    private static final Logger log = LoggerFactory.getLogger(GatewayMetricsConfig.class);
    
    /**
     * 自定义MeterRegistry，添加通用标签
     * @return MeterRegistryCustomizer
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "gateway-service");
    }
    
    /**
     * 全局过滤器，用于收集请求指标
     * @return GlobalFilter
     */
    @Bean
    @Order(0)
    public GlobalFilter metricsFilter(MeterRegistry meterRegistry) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().name();
            
            long startTime = System.currentTimeMillis();
            
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        int status = response.getStatusCode() != null ? response.getStatusCode().value() : 0;
                        
                        long duration = System.currentTimeMillis() - startTime;
                        
                        // 记录请求计数
                        meterRegistry.counter("gateway.requests", 
                                "path", path, 
                                "method", method, 
                                "status", String.valueOf(status))
                                .increment();
                        
                        // 记录请求延迟
                        meterRegistry.timer("gateway.request.duration", 
                                "path", path, 
                                "method", method, 
                                "status", String.valueOf(status))
                                .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
                        
                        log.debug("记录指标 - 路径: {}, 方法: {}, 状态: {}, 耗时: {}ms", path, method, status, duration);
                    }))
                    .onErrorResume(throwable -> {
                        // 记录错误请求
                        meterRegistry.counter("gateway.requests", 
                                "path", path, 
                                "method", method, 
                                "status", "500")
                                .increment();
                        
                        log.error("请求处理出错 - 路径: {}, 方法: {}, 错误: {}", path, method, throwable.getMessage());
                        return Mono.error(throwable);
                    })
                    .thenEmpty(Mono.empty());
        };
    }
}