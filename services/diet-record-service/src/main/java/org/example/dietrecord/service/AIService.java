package org.example.dietrecord.service;

import java.util.Map;

public interface AIService {
    /**
     * 使用 DeepSeek API 识别食物图片
     * @param imageBase64 图片的 Base64 编码
     * @return 识别结果，包含食物名称和营养成分
     */
    Map<String, Object> recognizeFoodImage(String imageBase64);
}

