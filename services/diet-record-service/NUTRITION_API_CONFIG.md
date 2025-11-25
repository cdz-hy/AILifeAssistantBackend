# 营养识别 API 配置说明

## 配置文件位置
`src/main/resources/application.properties`

## 配置项说明

### 1. nutrition.api.key
**说明：** 营养识别 API 的认证密钥（API Key）

**示例：**
```properties
nutrition.api.key=sk-1234567890abcdef
```

**获取方式：**
- 登录营养识别 API 服务提供商的网站
- 在开发者控制台或 API 管理页面创建 API Key
- 复制生成的 API Key 并粘贴到配置文件中

---

### 2. nutrition.api.url
**说明：** 营养识别 API 的端点地址（Endpoint URL）

**示例：**
```properties
# 示例1：通用营养识别 API
nutrition.api.url=https://api.nutrition-recognition.com/v1/recognize

# 示例2：自定义 API 地址
nutrition.api.url=https://your-domain.com/api/food/recognize

# 示例3：本地测试 API
nutrition.api.url=http://localhost:8080/api/recognize
```

**常见 API 提供商示例：**

#### 如果使用 OpenAI GPT-4 Vision：
```properties
nutrition.api.url=https://api.openai.com/v1/chat/completions
nutrition.api.type=json
```

#### 如果使用百度 AI：
```properties
nutrition.api.url=https://aip.baidubce.com/rest/2.0/image-classify/v2/dish
nutrition.api.type=multipart
```

#### 如果使用阿里云：
```properties
nutrition.api.url=https://food.cn-shanghai.aliyuncs.com/api/v1/recognize
nutrition.api.type=json
```

---

### 3. nutrition.api.type
**说明：** API 请求格式类型

**可选值：**
- `json`：使用 JSON 格式发送 Base64 编码的图片（默认）
- `multipart`：使用 multipart/form-data 格式上传图片文件

**示例：**
```properties
# JSON 格式（推荐，大多数 API 支持）
nutrition.api.type=json

# Multipart 格式（适用于文件上传 API）
nutrition.api.type=multipart
```

---

## 完整配置示例

### 示例1：使用 JSON 格式的 API
```properties
# 营养识别 API 配置
nutrition.api.key=your-actual-api-key-here
nutrition.api.url=https://api.nutrition-recognition.com/v1/recognize
nutrition.api.type=json
```

### 示例2：使用 Multipart 格式的 API
```properties
# 营养识别 API 配置
nutrition.api.key=your-actual-api-key-here
nutrition.api.url=https://api.nutrition-recognition.com/v1/upload
nutrition.api.type=multipart
```

---

## API 响应格式要求

代码支持多种响应格式，API 需要返回以下信息（至少包含食物名称）：

### 支持的响应格式：

#### 格式1：直接返回数据
```json
{
  "foodName": "鸡胸肉",
  "caloriesPer100g": 165,
  "proteinPer100g": 31,
  "fatPer100g": 3.6,
  "carbsPer100g": 0,
  "confidence": 0.95
}
```

#### 格式2：包含 data 字段
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "foodName": "鸡胸肉",
    "caloriesPer100g": 165,
    "proteinPer100g": 31,
    "fatPer100g": 3.6,
    "carbsPer100g": 0
  }
}
```

#### 格式3：包含 result 字段
```json
{
  "status": "ok",
  "result": {
    "foodName": "鸡胸肉",
    "caloriesPer100g": 165,
    "proteinPer100g": 31,
    "fatPer100g": 3.6,
    "carbsPer100g": 0
  }
}
```

#### 格式4：使用下划线命名
```json
{
  "food_name": "鸡胸肉",
  "calories_per_100g": 165,
  "protein_per_100g": 31,
  "fat_per_100g": 3.6,
  "carbs_per_100g": 0
}
```

### 字段名支持：
代码会自动识别以下字段名：
- 食物名称：`foodName`、`name`、`food_name`
- 热量：`caloriesPer100g`、`calories_per_100g`、`calories`
- 蛋白质：`proteinPer100g`、`protein_per_100g`、`protein`
- 脂肪：`fatPer100g`、`fat_per_100g`、`fat`
- 碳水：`carbsPer100g`、`carbs_per_100g`、`carbs`
- 置信度：`confidence`、`confidence_score`、`score`

---

## 测试配置

配置完成后，可以通过以下方式测试：

1. **启动后端服务**
2. **上传一张食物图片**
3. **查看后端日志**，检查 API 调用是否成功
4. **检查返回结果**，确认识别是否正确

---

## 常见问题

### Q: 如何知道应该使用 json 还是 multipart？
A: 查看 API 文档。如果 API 文档要求发送 JSON 格式的 Base64 图片，使用 `json`；如果要求上传文件，使用 `multipart`。

### Q: API Key 在哪里获取？
A: 登录你选择的营养识别 API 服务提供商的网站，在开发者控制台或 API 管理页面创建。

### Q: 如果 API 返回的字段名不同怎么办？
A: 代码已经支持多种常见的字段名格式。如果仍然不匹配，可以修改 `AIServiceImpl.java` 中的 `getStringValue` 和 `getDoubleValue` 方法，添加你需要的字段名。

### Q: 如何测试 API 是否配置正确？
A: 启动服务后，尝试上传一张食物图片。如果配置正确，应该能看到识别结果；如果配置错误，会在后端日志中看到错误信息。

---

## 注意事项

1. **API Key 安全**：不要将 API Key 提交到代码仓库，建议使用环境变量或配置文件（不提交到 Git）
2. **API 费用**：注意 API 调用的费用，避免超出预算
3. **请求限制**：注意 API 的请求频率限制，避免被限流
4. **错误处理**：如果 API 调用失败，系统会返回默认值，不会影响其他功能

