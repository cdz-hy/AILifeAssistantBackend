package org.example.dietrecord.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dietrecord.entity.FoodItem;
import org.example.dietrecord.service.AIService;
import org.example.dietrecord.service.FoodItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIServiceImpl implements AIService {

    @Value("${baidu.api.key:your-api-key}")
    private String baiduApiKey;

    @Value("${baidu.api.secret:your-api-secret}")
    private String baiduApiSecret;

    private static final String BAIDU_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private static final String BAIDU_COMBINATION_URL = "https://aip.baidubce.com/api/v1/solution/direct/imagerecognition/combination";
    
    private String accessToken;
    private long tokenExpireTime;

    private final FoodItemService foodItemService;
    private final ObjectMapper objectMapper;

    public AIServiceImpl(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> recognizeFoodImage(String imageBase64) {
        try {
            // 确保有有效的 access_token
            ensureAccessToken();
            
            // 调用百度组合服务 API（使用菜品识别）
            String response = callBaiduCombinationAPI(imageBase64);

            // 解析百度 API 响应
            JsonNode responseJson = objectMapper.readTree(response);
            
            // 检查是否有错误
            if (responseJson.has("error_code")) {
                int errorCode = responseJson.get("error_code").asInt();
                String errorMsg = responseJson.has("error_msg") ? 
                    responseJson.get("error_msg").asText() : "未知错误";
                throw new Exception("百度 API 错误: " + errorCode + " - " + errorMsg);
            }

            // 百度组合 API 返回格式：{"result": {"dishs": {"result": [...]}}}
            JsonNode resultObj = responseJson.get("result");
            if (resultObj == null || !resultObj.has("dishs")) {
                return createDefaultResult();
            }

            JsonNode dishsResult = resultObj.get("dishs");
            JsonNode dishsArray = dishsResult.get("result");
            
            if (dishsArray == null || !dishsArray.isArray() || dishsArray.size() == 0) {
                return createDefaultResult();
            }

            // 取第一个识别结果（置信度最高的）
            JsonNode firstResult = dishsArray.get(0);
            
            // 提取食物信息
            String foodName = firstResult.has("name") ? firstResult.get("name").asText() : "未知食物";
            double confidence = firstResult.has("probability") ? firstResult.get("probability").asDouble() : 0.9;
            
            // 百度 API 返回的热量信息
            // 百度返回格式：{"calorie": "165", "has_calorie": true}
            double caloriesPer100g = 0;
            if (firstResult.has("has_calorie") && firstResult.get("has_calorie").asBoolean()) {
                if (firstResult.has("calorie")) {
                    String calorieStr = firstResult.get("calorie").asText();
                    try {
                        // 百度返回的热量可能是每份的，这里假设是每100克的
                        caloriesPer100g = Double.parseDouble(calorieStr.replace("kcal", "").trim());
                    } catch (NumberFormatException e) {
                        // 如果解析失败，尝试从食物库获取
                    }
                }
            }
            
            // 百度 API 不直接返回营养成分，需要从食物库查询或使用默认值
            double proteinPer100g = 0;
            double fatPer100g = 0;
            double carbsPer100g = 0;

            // 尝试从食物库查找
            FoodItem foodItem = foodItemService.getFoodItemByName(foodName);
            Long foodItemId = null;

            // 如果营养成分为0但卡路里不为0，根据卡路里估算营养成分
            // 营养学公式：1g蛋白质=4kcal, 1g脂肪=9kcal, 1g碳水=4kcal
            // 使用常见的食物比例：蛋白质20%, 脂肪30%, 碳水50%
            double estimatedProtein = proteinPer100g;
            double estimatedFat = fatPer100g;
            double estimatedCarbs = carbsPer100g;
            boolean needsEstimation = false;
            
            if (caloriesPer100g > 0 && proteinPer100g == 0 && fatPer100g == 0 && carbsPer100g == 0) {
                // 估算每100克的营养成分
                // 假设总卡路里 = 蛋白质*4 + 脂肪*9 + 碳水*4
                // 使用比例：蛋白质20%, 脂肪30%, 碳水50%
                double totalCalories = caloriesPer100g;
                estimatedProtein = (totalCalories * 0.20) / 4.0;  // 20%的卡路里来自蛋白质
                estimatedFat = (totalCalories * 0.30) / 9.0;      // 30%的卡路里来自脂肪
                estimatedCarbs = (totalCalories * 0.50) / 4.0;   // 50%的卡路里来自碳水
                needsEstimation = true;
                
                System.out.println("根据卡路里估算营养成分 - 卡路里: " + caloriesPer100g + 
                        ", 蛋白质: " + estimatedProtein + "g, 脂肪: " + estimatedFat + "g, 碳水: " + estimatedCarbs + "g");
            }
            
            if (foodItem == null && !foodName.equals("未知食物") && caloriesPer100g > 0) {
                // 如果食物库中没有，创建新记录（包含估算的营养成分）
                FoodItem newFoodItem = new FoodItem();
                newFoodItem.setFoodName(foodName);
                newFoodItem.setCaloriesPer100g(java.math.BigDecimal.valueOf(caloriesPer100g));
                newFoodItem.setProteinPer100g(java.math.BigDecimal.valueOf(estimatedProtein));
                newFoodItem.setFatPer100g(java.math.BigDecimal.valueOf(estimatedFat));
                newFoodItem.setCarbsPer100g(java.math.BigDecimal.valueOf(estimatedCarbs));
                newFoodItem.setCreatedBy("ai");
                foodItem = foodItemService.createFoodItem(newFoodItem);
                System.out.println("创建新食物记录 - foodItemId: " + foodItem.getId() + 
                        ", 蛋白质: " + estimatedProtein + "g, 脂肪: " + estimatedFat + "g, 碳水: " + estimatedCarbs + "g");
            } else if (foodItem != null && needsEstimation) {
                // 如果食物已存在但营养成分为0，且我们估算出了营养成分，则更新食物记录
                boolean hasZeroNutrition = (foodItem.getProteinPer100g() == null || foodItem.getProteinPer100g().doubleValue() == 0) &&
                                         (foodItem.getFatPer100g() == null || foodItem.getFatPer100g().doubleValue() == 0) &&
                                         (foodItem.getCarbsPer100g() == null || foodItem.getCarbsPer100g().doubleValue() == 0);
                
                if (hasZeroNutrition) {
                    System.out.println("食物已存在但营养成分为0，更新营养成分 - foodItemId: " + foodItem.getId());
                    foodItem.setProteinPer100g(java.math.BigDecimal.valueOf(estimatedProtein));
                    foodItem.setFatPer100g(java.math.BigDecimal.valueOf(estimatedFat));
                    foodItem.setCarbsPer100g(java.math.BigDecimal.valueOf(estimatedCarbs));
                    foodItem = foodItemService.updateFoodItem(foodItem);
                    System.out.println("更新食物记录成功 - foodItemId: " + foodItem.getId() + 
                            ", 蛋白质: " + estimatedProtein + "g, 脂肪: " + estimatedFat + "g, 碳水: " + estimatedCarbs + "g");
                }
            }

            if (foodItem != null) {
                foodItemId = foodItem.getId();
                // 使用食物库的数据（更准确）
                caloriesPer100g = foodItem.getCaloriesPer100g().doubleValue();
                proteinPer100g = foodItem.getProteinPer100g() != null ? foodItem.getProteinPer100g().doubleValue() : 0;
                fatPer100g = foodItem.getFatPer100g() != null ? foodItem.getFatPer100g().doubleValue() : 0;
                carbsPer100g = foodItem.getCarbsPer100g() != null ? foodItem.getCarbsPer100g().doubleValue() : 0;
                
                System.out.println("从食物库读取营养成分 - foodItemId: " + foodItemId + 
                        ", 蛋白质: " + proteinPer100g + "g, 脂肪: " + fatPer100g + "g, 碳水: " + carbsPer100g + "g");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("foodName", foodName);
            result.put("calories", caloriesPer100g);
            result.put("protein", proteinPer100g);
            result.put("fat", fatPer100g);
            result.put("carbs", carbsPer100g);
            result.put("foodItemId", foodItemId);
            result.put("confidence", confidence);
            
            System.out.println("返回给前端的识别结果 - 食物名称: " + foodName + 
                    ", foodItemId: " + foodItemId + 
                    ", 蛋白质: " + proteinPer100g + "g, 脂肪: " + fatPer100g + "g, 碳水: " + carbsPer100g + "g");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultResult();
        }
    }

    /**
     * 确保 access_token 有效，如果过期则重新获取
     */
    private void ensureAccessToken() throws Exception {
        // 如果 token 不存在或即将过期（提前5分钟刷新），则重新获取
        if (accessToken == null || System.currentTimeMillis() >= tokenExpireTime - 300000) {
            accessToken = getBaiduAccessToken();
            // Token 有效期通常是30天，这里设置为29天（毫秒）
            tokenExpireTime = System.currentTimeMillis() + (29L * 24 * 60 * 60 * 1000);
        }
    }

    /**
     * 获取百度 API access_token（按照官方示例实现）
     */
    private String getBaiduAccessToken() throws Exception {
        // 检查配置是否正确
        if (baiduApiKey == null || baiduApiKey.equals("your-baidu-api-key") || baiduApiKey.isEmpty()) {
            throw new Exception("百度 API Key 未配置，请在 application.properties 中设置 baidu.api.key");
        }
        if (baiduApiSecret == null || baiduApiSecret.equals("your-baidu-api-secret") || baiduApiSecret.isEmpty()) {
            throw new Exception("百度 API Secret 未配置，请在 application.properties 中设置 baidu.api.secret");
        }

        // 去除可能的空格和换行符
        baiduApiKey = baiduApiKey.trim();
        baiduApiSecret = baiduApiSecret.trim();

        System.out.println("正在获取 access_token");
        System.out.println("API Key 长度: " + baiduApiKey.length() + ", 前10个字符: " + baiduApiKey.substring(0, Math.min(10, baiduApiKey.length())));
        System.out.println("Secret Key 长度: " + baiduApiSecret.length() + ", 前10个字符: " + baiduApiSecret.substring(0, Math.min(10, baiduApiSecret.length())));

        // 按照官方示例，使用 POST 请求，参数放在请求体中
        String params = "grant_type=client_credentials&client_id=" + baiduApiKey
                + "&client_secret=" + baiduApiSecret;
        
        URL url = new URL(BAIDU_TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        // 写入请求体
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        
        // 读取响应
        String responseBody;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responseBody = response.toString();
            }
        } else {
            // 读取错误响应
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                responseBody = errorResponse.toString();
            }
            System.err.println("获取 access_token 失败，状态码: " + responseCode + ", 响应: " + responseBody);
            throw new Exception("获取 access_token 失败，状态码: " + responseCode + 
                    (responseBody != null && !responseBody.isEmpty() ? ", 响应: " + responseBody : 
                    "。请检查 baidu.api.key 和 baidu.api.secret 是否正确配置"));
        }

        // 解析响应
        try {
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            if (jsonResponse.has("access_token")) {
                String token = jsonResponse.get("access_token").asText();
                System.out.println("成功获取 access_token");
                return token;
            } else if (jsonResponse.has("error")) {
                String error = jsonResponse.get("error").asText();
                String errorDescription = jsonResponse.has("error_description") ? 
                    jsonResponse.get("error_description").asText() : "";
                throw new Exception("获取 access_token 失败: " + error + 
                        (errorDescription.isEmpty() ? "" : " - " + errorDescription));
            } else {
                throw new Exception("获取 access_token 失败: 响应格式不正确 - " + responseBody);
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new Exception("解析 access_token 响应失败: " + responseBody, e);
        }
    }

    /**
     * 调用百度组合服务 API（按照官方示例实现）
     */
    private String callBaiduCombinationAPI(String imageBase64) throws Exception {
        // 按照官方示例，使用组合服务接口
        String urlStr = BAIDU_COMBINATION_URL + "?access_token=" + accessToken;
        
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setDoOutput(true);

        // 构建请求体 JSON
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("image", imageBase64);
        // 指定使用菜品识别服务
        requestBody.put("scenes", java.util.Arrays.asList("dishs"));
        // sceneConf 可选，这里不设置使用默认值

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            // 读取错误响应
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                throw new Exception("百度 API 调用失败，状态码: " + responseCode + ", 错误信息: " + errorResponse.toString());
            }
        }
    }

    private Map<String, Object> createDefaultResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("foodName", "未知食物");
        result.put("calories", 0);
        result.put("protein", 0);
        result.put("fat", 0);
        result.put("carbs", 0);
        result.put("foodItemId", null);
        result.put("confidence", 0.0);
        return result;
    }
}

