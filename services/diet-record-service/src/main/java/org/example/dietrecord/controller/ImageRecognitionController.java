package org.example.dietrecord.controller;

import org.example.dietrecord.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/diet-logs/image-recognition")
public class ImageRecognitionController {

    private final AIService aiService;

    public ImageRecognitionController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> recognizeFoodImage(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam("image") MultipartFile image) {
        try {
            // 将图片转换为 Base64
            byte[] imageBytes = image.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            // 调用 AI 服务识别食物
            Map<String, Object> result = aiService.recognizeFoodImage(imageBase64);

            System.out.println("图片识别请求 - 用户: " + userId + ", 文件名: " + image.getOriginalFilename() + 
                    ", 识别结果: " + result.get("foodName"));

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

