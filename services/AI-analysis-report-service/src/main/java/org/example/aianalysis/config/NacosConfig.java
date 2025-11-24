package org.example.aianalysis.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * Nacos配置中心配置类
 * 用于从Nacos配置中心获取大模型API地址和API Key
 */
@Component
@RefreshScope
public class NacosConfig {
    
    @Value("${ai.model.api.url:}")
    private String aiModelApiUrl;
    
    @Value("${ai.model.api.key:}")
    private String aiModelApiKey;
    
    // 默认城市编码
    @Value("${default.city.code:360113}")
    private String defaultCityCode;
    
    public String getAiModelApiUrl() {
        return aiModelApiUrl;
    }
    
    public String getAiModelApiKey() {
        return aiModelApiKey;
    }
    
    public String getDefaultCityCode() {
        return defaultCityCode;
    }
}