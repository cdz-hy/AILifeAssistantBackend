# AI Life Assistant Backend

AI智能生活助手 -- 后端微服务系统。采用 Spring Cloud 微服务架构，通过 Nacos 实现服务注册与配置中心管理，Spring Cloud Gateway 统一路由转发，集成 JobRunr 构建分布式后台任务调度，利用大语言模型 (LLM) 对用户的生活习惯进行多维度智能分析与建议。

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 / 框架 | Java 21, Spring Boot 3.3.4, Spring Cloud 2023.0.3 |
| 微服务治理 | Spring Cloud Alibaba 2023.0.3.2, Nacos (服务发现 + 配置中心) |
| API 网关 | Spring Cloud Gateway (动态路由, 全局过滤器, Prometheus 监控) |
| 服务间调用 | Spring Cloud OpenFeign + Spring Cloud LoadBalancer |
| 数据持久化 | MyBatis (注解式 SQL), Spring Data JPA, MySQL 8 |
| 后台任务调度 | JobRunr 6.3.3 / 7.0.0 (延迟任务, 仪表盘) |
| AI 集成 | 讯飞星火大模型 (OpenAI 兼容 Chat Completions 协议) |
| 图像识别 | 百度 AI 开放平台 (菜品识别) |
| 天气服务 | 高德地图 Web API |
| 认证鉴权 | JWT (jjwt 0.11.5) + BCrypt |
| 监控 | Spring Boot Actuator + Micrometer + Prometheus |
| 构建工具 | Maven (多模块) |

## 系统架构

```
                            +-------------------+
                            |    Client (Web)    |
                            +--------+----------+
                                     |
                                     v
                            +--------+----------+
                            |   Gateway Service  |  :8000
                            |  (Spring Cloud GW) |
                            +--------+----------+
                                     |
              +----------+-----------+-----------+----------+----------+----------+
              |          |           |           |          |          |          |
              v          v           v           v          v          v          v
       +----------+ +----------+ +----------+ +----------+ +----------+ +----------+ +----------+
       |  User    | | Schedule | | Finance  | |   Diet   | |  Study   | |    AI    | |Notification|
       | Service  | | Service  | | Service  | |  Record  | | Service  | | Analysis | |  Service   |
       |  :8001   | |  :8002   | |  :8003   | |  :8004   | |  :8005   | |  :8006   | |   :8007    |
       +----+-----+ +----+-----+ +----+-----+ +----+-----+ +----+-----+ +----+-----+ +----+------+
            |             |           |           |             |           |             |
            v             v           v           v             v           v             v
       +----+--------+   +-----------+-----------+-------------+-----------+------+------+
       | user_service |   |         各服务独立 MySQL 数据库 (共 7 个, 30 张表)            |
       |     _db      |   +----------------------------------------------------------+
       +--------------+
```

**Nacos** 作为注册中心与配置中心，所有服务启动时自动注册，配置项支持运行时动态刷新 (`@RefreshScope`)。

## 微服务概览

| 服务 | 端口 | 职责 |
|------|------|------|
| gateway-service | 8000 | API 网关，统一路由转发、CORS、日志、Prometheus 指标采集 |
| user-service | 8001 | 用户认证 (JWT)、个人资料、头像、设备管理、数据授权、画像标签 |
| schedule-service | 8002 | 日程 CRUD、重复规则 (RRule)、提醒、AI 建议、天气查询 |
| finance-service | 8003 | 收支记录、预算管理、分类体系、月度统计 |
| diet-record-service | 8004 | 饮食记录、食物库、营养追踪、AI 图像识别 |
| study-service | 8005 | 学习计划、学习任务、专注计时 (Pomodoro)、学习统计与推荐 |
| ai-analysis-report-service | 8006 | LLM 驱动的多维度生活分析 (日程/财务/饮食/学习/综合) |
| notification-service | 8007 | 通知发送、免打扰、通知偏好管理 |

## 功能详情

### 用户服务 (user-service)

- **认证鉴权**: 注册、登录、登出，JWT (HS256) 签发与校验，内存级 Token 黑名单
- **个人资料**: 昵称、头像上传/访问 (本地文件存储, 10MB 限制, 支持 jpg/png/webp/gif)
- **设备管理**: 绑定/替换推送设备 Token
- **数据授权**: 按数据类型 (健康分析、财务分析) 的用户授权控制
- **用户画像**: 键值对形式的画像指标，支持用户自定义输入和 AI 自动更新

**核心实体**: `UserCore` (t_user), `UserProfile` (t_user_profile), `BoundDevice` (t_bound_device), `DataAuthorization` (t_data_authorization), `PersonaMetric` (t_user_persona_metric)

### 日程服务 (schedule-service)

- **日程管理**: 完整 CRUD，支持按状态、日期范围、紧急/重要四象限 (Eisenhower Matrix) 筛选
- **重复规则**: 自研 iCal RRULE 解析/生成器，支持 DAILY/WEEKLY/MONTHLY/YEARLY 及自定义频率，支持 BYDAY/BYMONTH/BYMONTHDAY 等修饰符
- **生命周期自动化**: 基于 JobRunr 的三阶段延迟任务 (提醒 -> 开始 -> 结束)，含僵尸任务检测，用户修改日程时自动重新调度
- **标签与分类**: 日程类型 (带颜色)、标签体系，支持系统预设与用户自定义
- **AI 建议**: 关联日程的 AI 优化建议 (冲突检测、改期方案)
- **天气集成**: 高德天气 API 调用，MySQL 缓存 + 1 小时 TTL，支持实时/预报两种模式
- **启动恢复**: `ScheduleInitializer` 在服务重启时自动重建未来 30 天的 JobRunr 任务

**核心实体**: `Schedule` (t_schedule), `ScheduleType` (t_schedule_type), `ScheduleTag` (t_schedule_tag), `ScheduleReminder` (t_schedule_reminder), `ScheduleAISuggestion` (t_schedule_ai_suggestion), `WeatherCache` (t_weather_cache)

### 财务服务 (finance-service)

- **收支记录**: 创建、删除、查询 (最近 20 条、月度统计)
- **预算管理**: 按月/按分类预算，实时计算已用金额与剩余金额，超支检测
- **分类体系**: 树形分类结构 (自引用 parent_id)，系统预设 + 用户自定义，递归构建分类树
- **月度统计**: 按月汇总收入/支出总额

**核心实体**: `Transaction` (t_transaction), `FinanceCategory` (t_finance_category), `Budget` (t_budget)

### 饮食记录服务 (diet-record-service)

- **饮食日志**: 按日期记录每餐 (早/午/晚/加餐)，支持按餐次筛选
- **食物库**: 食物营养数据库 (热量/蛋白质/脂肪/碳水/100g)，模糊搜索
- **营养追踪**: 每餐自动计算热量，每日汇总四大营养素并与目标值对比
- **AI 图像识别**: 集成百度 AI 菜品识别，上传食物照片自动返回食物名称、热量、营养素估算，新识别的食物自动入库

**核心实体**: `FoodItem` (t_food_item), `DietLog` (t_diet_log), `DietEntry` (t_diet_log_entry)

### 学习服务 (study-service)

- **学习计划**: 计划 CRUD，支持目标、日期范围、状态管理
- **学习任务**: 任务 CRUD，支持关联计划或独立存在，完成/未完成状态切换
- **专注计时**: Pomodoro 风格专注会话，启动 -> 完成/中断状态流转，可关联学习任务
- **学习统计**: 计划完成率、任务完成率、总专注时长，支持日期范围筛选
- **智能推荐**: 基于历史数据分析最佳学习时段、紧急任务提醒、学习一致性检查

**核心实体**: `StudyPlan` (t_study_plan), `StudyTask` (t_study_task), `FocusSession` (t_focus_session)

### AI 分析服务 (ai-analysis-report-service)

核心 AI 引擎，通过 OpenFeign 跨服务采集用户数据，结合 LLM 生成多维度生活分析报告。

**分析类型**:

| 报告类型 | 分析服务 | 角色定位 | 分析维度 |
|----------|----------|----------|----------|
| 每日综合 | ComprehensiveAnalysisService | AI 生活总管 | 跨域效率评分 (日程30% + 学习30% + 财务20% + 饮食20%)、用户画像更新 |
| 每日日程 | ScheduleAnalysisService | 时间管理顾问 | 天气提醒、精力管理、冲突预警、准备建议 |
| 每日财务 | FinanceAnalysisService | 私人理财顾问 | 预算预警 (70%/90% 阈值)、消费习惯、收支平衡 |
| 每日饮食 | DietAnalysisService | 营养专家 | 能量管理、环境驱动饮食调整、营养陷阱 |
| 每日学习 | StudyAnalysisService | 学习效能教练 | PDCA 循环、专注力分析、拖延预警、方法建议 |

**LLM 集成**:
- 模型: 讯飞星火 4.0 Ultra
- 协议: OpenAI 兼容 Chat Completions
- 限流: 同步锁 + 1 秒最小间隔
- 配置: Nacos 动态管理 API 密钥与端点

**调度策略**:
- 每日报告: 每 30 分钟触发 (8:00-22:00)，覆盖所有用户
- 周报: 每周一 08:00
- 月报: 每月 1 日 03:00
- 支持按需请求: GET (缓存优先) 和 POST (强制重新生成) 两种模式

### 通知服务 (notification-service)

- **通知发送**: 支持 push/websocket/sms 三种通道 (当前为存根实现)
- **免打扰**: 每用户独立的 DND 时间窗口，正确处理跨日场景 (如 22:00-06:00)，高优先级通知可穿透
- **通知偏好**: 按事件类型 + 通道 + 提醒方式的细粒度控制
- **通知管理**: 已读标记 (单条/全部)、删除

**核心实体**: `NotificationLog` (t_notification_log), `NotificationDnd` (t_notification_dnd), `NotificationPreference` (t_notification_preference)

### 网关服务 (gateway-service)

- **路由转发**: 7 条动态路由，基于 Nacos 服务发现 + 负载均衡 (`lb://` 协议)
- **路径剥离**: `StripPrefix=1`，客户端通过首段路径路由到目标服务
- **全局过滤器**: 请求日志 (方法、路径、耗时)、Micrometer 指标采集 (请求计数、耗时分布)
- **CORS**: 统一跨域配置 (支持 localhost:8080)
- **监控端点**: Actuator health/info/metrics/prometheus/gateway

| 路由前缀 | 目标服务 |
|----------|----------|
| `/user/**` | user-service |
| `/schedule/**` | schedule-service |
| `/finance/**` | finance-service |
| `/diet/**` | diet-record-service |
| `/study/**` | study-service |
| `/ai-analysis/**` | ai-analysis-report-service |
| `/notification/**` | notification-service |

## 数据库设计

系统使用 7 个独立 MySQL 数据库，共 30 张表，采用 `utf8mb4` 字符集:

| 数据库 | 表数量 | 核心表 |
|--------|--------|--------|
| user_service_db | 7 | t_user, t_user_profile, t_bound_device, t_data_authorization, t_user_persona_metric, t_persona_tag, t_user_persona_tag_map |
| schedule_service_db | 8 | t_schedule, t_schedule_type, t_schedule_tag, t_schedule_tag_map, t_schedule_exception, t_schedule_reminder, t_schedule_ai_suggestion, t_weather_cache |
| finance_service_db | 3 | t_transaction, t_finance_category, t_budget |
| diet_service_db | 3 | t_food_item, t_diet_log, t_diet_log_entry |
| study_service_db | 3 | t_study_plan, t_study_task, t_focus_session |
| ai_analysis_service_db | 3 | t_generated_report, t_agg_daily_metrics, t_ai_model_info |
| notification_service_db | 3 | t_notification_log, t_notification_preference, t_notification_dnd |

设计特点:
- 所有主键使用 `BIGINT AUTO_INCREMENT`
- 跨服务无外键约束，服务间通过 API 解耦
- 多对多关系使用独立关联表
- AI 生成数据使用 `JSON` 类型存储
- 查找表支持系统预设 (user_id 为 NULL) 和用户自定义

## 项目结构

```
AILifeAssistantBackend/
├── pom.xml                          # 根 POM (Spring Boot 3.3.4 parent)
├── docs/                            # 设计文档
│   ├── 微服务划分与功能说明.md
│   ├── 数据库设计说明.md
│   ├── 数据库MySQL建表代码.md
│   ├── schedule-api.md
│   └── postman-testing-guide.md
└── services/                        # 微服务聚合模块
    ├── pom.xml
    ├── gateway-service/             # API 网关
    ├── user-service/                # 用户服务
    ├── schedule-service/            # 日程服务
    ├── finance-service/             # 财务服务
    ├── diet-record-service/         # 饮食记录服务
    ├── study-service/               # 学习服务
    ├── ai-analysis-report-service/  # AI 分析服务
    └── notification-service/        # 通知服务
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Nacos 2.x (默认地址 127.0.0.1:8848)

### 数据库初始化

执行 `docs/数据库MySQL建表代码.md` 中的 SQL 脚本，创建 7 个数据库及全部表。

### 构建

```bash
mvn clean package -DskipTests
```

### 启动顺序

1. 启动 Nacos Server
2. 启动 user-service (:8001)
3. 启动 schedule-service (:8002)
4. 启动 finance-service (:8003)
5. 启动 diet-record-service (:8004)
6. 启动 study-service (:8005)
7. 启动 notification-service (:8007)
8. 启动 ai-analysis-report-service (:8006)
9. 启动 gateway-service (:8000)

所有业务服务通过 Nacos 自动注册，网关通过服务发现动态路由。

### Nacos 配置

以下配置项需在 Nacos 配置中心管理:

| 配置项 | 服务 | 说明 |
|--------|------|------|
| `ai.model.api.url` | ai-analysis-report-service | 讯飞星火 API 地址 |
| `ai.model.api.key` | ai-analysis-report-service | 讯飞星火 API 密钥 |
| `default.city.code` | ai-analysis-report-service | 默认城市编码 (高德天气) |
| `Key` | schedule-service | 高德地图 API Key |
| `Weather.extensions` | schedule-service | 天气查询模式 (base/all) |

### 访问地址

| 服务 | 地址 |
|------|------|
| API 网关 | http://localhost:8000 |
| Nacos 控制台 | http://localhost:8848/nacos |
| JobRunr 仪表盘 (日程) | http://localhost:8008 |
| JobRunr 仪表盘 (AI分析) | http://localhost:8086 |
| Actuator 健康检查 | http://localhost:8000/actuator/health |
| Prometheus 指标 | http://localhost:8000/actuator/prometheus |

## API 概览

所有请求通过网关 (localhost:8000) 转发，路径前缀对应目标服务。

### 用户认证

```
POST   /user/auth/register          # 注册
POST   /user/auth/login             # 登录
POST   /user/auth/logout            # 登出
```

### 用户资料

```
GET    /user/users/me               # 获取当前用户
PUT    /user/users/me               # 更新资料
POST   /user/users/me/avatar        # 上传头像
PUT    /user/users/me/authorizations # 数据授权
PUT    /user/users/me/devices       # 设备管理
GET    /user/users/me/persona       # 用户画像
PUT    /user/users/me/persona       # 更新画像
```

### 日程管理

```
GET    /schedule/api/schedules/user/{userId}           # 用户日程列表
GET    /schedule/api/schedules/user/{userId}/range      # 按日期范围查询
GET    /schedule/api/schedules/user/{userId}/urgency    # 四象限筛选
POST   /schedule/api/schedules                          # 创建日程
PUT    /schedule/api/schedules/{id}                     # 更新日程
DELETE /schedule/api/schedules/{id}                     # 删除日程
POST   /schedule/api/recurrence-patterns/to-rrule       # 转换 RRule
POST   /schedule/api/recurrence-patterns/from-rrule     # 解析 RRule
GET    /schedule/api/weather/city/{cityCode}            # 天气查询
```

### 财务管理

```
GET    /finance/api/finance/transactions                # 收支列表
POST   /finance/api/finance/transactions                # 记录收支
DELETE /finance/api/finance/transactions/{id}           # 删除记录
GET    /finance/api/finance/transactions/stats/monthly  # 月度统计
GET    /finance/api/finance/budgets                     # 预算列表
POST   /finance/api/finance/budgets                     # 创建预算
GET    /finance/api/finance/categories/tree             # 分类树
```

### 饮食记录

```
GET    /diet/api/diet-logs/{userId}/{date}              # 日饮食记录
POST   /diet/api/diet-logs/{userId}/{date}/entries      # 添加饮食条目
GET    /diet/api/diet-logs/{userId}/{date}/stats        # 营养统计
POST   /diet/api/diet-logs/image-recognition            # AI 图像识别
GET    /diet/api/food-items/search?foodName=...         # 食物搜索
```

### 学习管理

```
GET    /study/api/study-plans/user/{userId}             # 学习计划列表
POST   /study/api/study-plans                           # 创建计划
GET    /study/api/study-tasks/user/{userId}             # 任务列表
POST   /study/api/study-tasks/{id}/complete             # 完成任务
POST   /study/api/focus/start                           # 开始专注
POST   /study/api/focus/{sessionId}/complete            # 结束专注
GET    /study/api/focus/user/{userId}/total-time        # 总学习时长
```

### AI 分析报告

```
GET    /ai-analysis/api/ai-analysis/daily/{userId}      # 获取每日综合报告
GET    /ai-analysis/api/ai-analysis/daily-schedule/{userId}  # 每日日程分析
GET    /ai-analysis/api/ai-analysis/daily-finance/{userId}   # 每日财务分析
GET    /ai-analysis/api/ai-analysis/daily-diet/{userId}      # 每日饮食分析
GET    /ai-analysis/api/ai-analysis/daily-study/{userId}     # 每日学习分析
POST   /ai-analysis/api/ai-analysis/daily/{userId}      # 强制重新生成
```

### 通知管理

```
POST   /notification/api/notifications/send             # 发送通知
GET    /notification/api/notifications/user/{userId}    # 通知列表
PUT    /notification/api/notifications/{id}/read        # 标记已读
POST   /notification/api/dnd/user/{userId}              # 设置免打扰
POST   /notification/api/preferences/user/{userId}      # 通知偏好
```

## 许可证

本项目仅用于学术课程设计用途。
