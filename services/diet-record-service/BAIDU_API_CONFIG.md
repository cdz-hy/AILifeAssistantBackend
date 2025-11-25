# 百度 AI 食物识别 API 配置说明

## 配置步骤

### 1. 注册百度 AI 开放平台账号

1. 访问：https://ai.baidu.com/
2. 注册并登录账号

### 2. 创建应用

1. 登录后，进入 **控制台** → **应用列表**
2. 点击 **创建应用**
3. 选择 **图像识别** → **菜品识别**
4. 填写应用信息：
   - 应用名称：饮食记录识别（自定义）
   - 应用描述：用于识别食物图片（可选）
5. 创建成功后，会获得：
   - **API Key**
   - **Secret Key**

### 3. 配置 application.properties

在 `src/main/resources/application.properties` 文件中配置：

```properties
# 百度 AI 食物识别 API 配置
baidu.api.key=你的API_Key
baidu.api.secret=你的Secret_Key
```

**示例：**
```properties
baidu.api.key=abc123def456ghi789
baidu.api.secret=xyz789uvw456rst123
```

## 配置说明

### baidu.api.key
- **说明：** 百度 AI 应用的 API Key
- **获取位置：** 百度 AI 控制台 → 应用列表 → 你的应用 → API Key
- **格式：** 字符串，例如：`abc123def456ghi789`

### baidu.api.secret
- **说明：** 百度 AI 应用的 Secret Key
- **获取位置：** 百度 AI 控制台 → 应用列表 → 你的应用 → Secret Key
- **格式：** 字符串，例如：`xyz789uvw456rst123`

## 工作原理

1. **自动获取 Access Token**
   - 系统会自动使用 API Key 和 Secret Key 获取 access_token
   - Token 有效期 30 天，系统会自动刷新（提前 5 分钟）

2. **调用食物识别 API**
   - 使用 access_token 调用百度食物识别接口
   - 发送 Base64 编码的图片

3. **解析识别结果**
   - 解析百度 API 返回的食物名称和热量信息
   - 如果食物库中有该食物，使用食物库的详细营养成分
   - 如果食物库中没有，创建新记录（仅包含热量，营养成分需要后续补充）

## 百度 API 响应格式

百度 API 返回格式示例：

```json
{
  "log_id": 123456789,
  "result_num": 1,
  "result": [
    {
      "name": "鸡胸肉",
      "calorie": "165",
      "has_calorie": true,
      "probability": 0.95
    }
  ]
}
```

## 注意事项

1. **API 配额限制**
   - 免费版有每日调用次数限制
   - 注意查看控制台的配额使用情况

2. **营养成分补充**
   - 百度 API 只返回食物名称和热量
   - 蛋白质、脂肪、碳水化合物需要：
     - 从食物库查询（如果已存在）
     - 手动补充到食物库
     - 或使用其他营养数据库 API 补充

3. **图片格式要求**
   - 支持 JPG、PNG 格式
   - 图片大小建议不超过 4MB
   - Base64 编码后不超过 10MB

4. **错误处理**
   - 如果 API 调用失败，会返回默认值
   - 错误信息会记录在日志中
   - 常见错误码：
     - `110`：Access Token 无效
     - `17`：每天请求量超限额
     - `19`：QPS超限额

## 测试配置

配置完成后，测试步骤：

1. **启动后端服务**
2. **上传一张食物图片**（如：鸡胸肉、米饭等常见食物）
3. **查看后端日志**，确认：
   - Access Token 获取成功
   - API 调用成功
   - 识别结果正确
4. **检查前端**，确认：
   - 食物名称显示正确
   - 热量信息显示正确

## 常见问题

### Q: 如何查看 API Key 和 Secret Key？
A: 登录百度 AI 控制台 → 应用列表 → 点击你的应用 → 查看应用详情

### Q: API 调用失败怎么办？
A: 
1. 检查 API Key 和 Secret Key 是否正确
2. 检查网络连接是否正常
3. 查看后端日志中的错误信息
4. 确认 API 配额是否用完

### Q: 为什么只显示热量，没有蛋白质等信息？
A: 百度 API 只返回食物名称和热量。营养成分需要：
- 从食物库查询（如果已存在）
- 手动添加到食物库
- 或集成其他营养数据库 API

### Q: 如何补充营养成分数据？
A: 
1. 在食物库中手动添加常见食物的营养成分
2. 或集成其他营养数据库 API（如：USDA FoodData Central）
3. 或使用爬虫从营养网站获取数据

## 相关链接

- 百度 AI 开放平台：https://ai.baidu.com/
- 菜品识别 API 文档：https://ai.baidu.com/ai-doc/IMAGERECOGNITION/tk3bcxe0r
- 应用管理控制台：https://console.bce.baidu.com/ai/

