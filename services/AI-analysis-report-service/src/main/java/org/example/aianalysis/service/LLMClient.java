package org.example.aianalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.aianalysis.config.NacosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 大语言模型客户端
 * 用于调用SparkLite大模型API
 */
@Service
public class LLMClient {
    
    @Autowired
    private NacosConfig nacosConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 添加简单的速率限制，避免QPS超限
    private static final Object lock = new Object();
    private static long lastRequestTime = 0;
    private static final long MIN_INTERVAL_MS = 1000; // 最小间隔1秒
    
    /**
     * 调用大语言模型API
     * @param systemPrompt 系统提示词
     * @param userPrompt 用户提示词数据
     * @return 模型响应结果
     */
    public String chat(String systemPrompt, String userPrompt) {
        try {
            // 从Nacos配置中心获取API地址和Key
            String apiUrl = nacosConfig.getAiModelApiUrl();
            String apiKey = nacosConfig.getAiModelApiKey();
            
            if (apiUrl == null || apiUrl.isEmpty() || apiKey == null || apiKey.isEmpty()) {
                System.err.println("大语言模型API配置未完成");
                return null;
            }
            
            // 简单的速率限制，确保请求之间有足够的时间间隔
            synchronized (lock) {
                long currentTime = System.currentTimeMillis();
                long timeSinceLastRequest = currentTime - lastRequestTime;
                if (timeSinceLastRequest < MIN_INTERVAL_MS) {
                    try {
                        Thread.sleep(MIN_INTERVAL_MS - timeSinceLastRequest);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("等待下一请求时被中断: " + e.getMessage());
                        return null;
                    }
                }
                lastRequestTime = System.currentTimeMillis();
            }
            
            // 构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            // 使用Jackson构造请求体，避免字符串拼接带来的转义问题
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "4.0Ultra"); // 讯飞星火Spark Lite模型的正确参数名为"lite"
            
            ArrayNode messagesArray = objectMapper.createArrayNode();
            
            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            messagesArray.add(systemMessage);
            
            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", userPrompt);
            messagesArray.add(userMessage);
            
            requestBody.set("messages", messagesArray);
            
            String requestBodyStr = objectMapper.writeValueAsString(requestBody);
            
            HttpEntity<String> request = new HttpEntity<>(requestBodyStr, headers);
            
            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            
            return response.getBody();
        } catch (Exception e) {
            System.err.println("调用大语言模型API异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}