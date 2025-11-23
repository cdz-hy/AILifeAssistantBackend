# AI分析报告服务

## 简介

AI分析报告服务是AI生活助手的核心服务之一，负责收集其他微服务的数据并进行智能分析，生成各类分析报告。

## 功能特性

1. **定时数据分析**：每2小时主动调用其他服务API获取数据进行分析
2. **报告生成**：
   - 日报：每日分析报告
   - 周报：每周分析报告
   - 月报：每月分析报告
3. **按需分析**：在前端请求时进行实时分析（若当日尚未分析）
4. **任务调度**：使用JobRunr进行任务调度和管理

## 技术架构

- Spring Boot
- MyBatis
- JobRunr（任务调度）
- Nacos（服务发现）

## 数据库表结构

### t_generated_report（分析报告存储表）
- id: 报告唯一标识
- user_id: 用户ID
- report_type: 报告类型（daily, weekly, monthly）
- start_date: 报告涵盖的开始日期
- end_date: 报告涵盖的结束日期
- report_data_json: 报告详细内容（JSON格式）
- created_at: 报告生成时间

### t_agg_daily_metrics（每日聚合指标表）
- id: 唯一标识
- user_id: 用户ID
- date: 指标对应的日期
- total_calories: 当日总摄入卡路里
- total_focus_minutes: 当日总专注分钟
- total_expense: 当日总支出

### t_ai_model_info（AI模型信息表）
- id: 模型唯一标识
- model_name: AI模型名称
- version: 模型版本号
- description: 模型描述

## API接口

### 获取日报
```
GET /api/ai-analysis/daily/{userId}?date={date}
```

### 获取周报
```
GET /api/ai-analysis/weekly/{userId}?date={date}
```

### 获取月报
```
GET /api/ai-analysis/monthly/{userId}?date={date}
```

### 主动触发日报生成
```
POST /api/ai-analysis/daily/{userId}?date={date}
```

### 主动触发周报生成
```
POST /api/ai-analysis/weekly/{userId}?date={date}
```

### 主动触发月报生成
```
POST /api/ai-analysis/monthly/{userId}?date={date}
```

## 任务调度

1. **每2小时执行一次数据收集和分析任务**（8:00 - 22:00期间）
2. **每周一凌晨2点执行周报生成任务**
3. **每月1号凌晨3点执行月报生成任务**

## 配置说明

配置文件：`src/main/resources/application.properties`

主要配置项：
- 服务端口：8006
- Nacos地址：127.0.0.1:8848
- 数据库连接信息
- JobRunr配置

## 开发说明

1. 数据收集和AI分析部分使用占位符实现，实际项目中需要替换为真实的API调用
2. AI分析结果以JSON格式存储在数据库中
3. 任务调度使用JobRunr实现，支持分布式部署