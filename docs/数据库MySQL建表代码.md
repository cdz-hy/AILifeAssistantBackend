-- -----------------------------------------------------------
-- AI生活助手 - 各微服务数据库创建脚本 (MySQL)
-- -----------------------------------------------------------
-- 脚本总览:
-- 1. 创建 7 个独立的数据库 (Database / Schema)
--    - user_service_db
--    - schedule_service_db
--    - finance_service_db
--    - diet_service_db
--    - study_service_db
--    - ai_analysis_service_db
--    - notification_service_db
-- 2. 在每个数据库中创建其所属的表
-- 3. 设置字符集为 utf8mb4 以支持表情和多语言
-- 4. 仅在数据库内部建立外键, 不跨数据库
-- -----------------------------------------------------------

-- -----------------------------------------------------------
-- 1. 用户服务 (user_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS user_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE user_service_db;

-- 1.1 用户核心表
CREATE TABLE IF NOT EXISTS t_user (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键, 用户ID',
    `username` VARCHAR(100) NOT NULL COMMENT '登录用户名',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码哈希值',
    `email` VARCHAR(255) NULL COMMENT '电子邮箱',
    `phone` VARCHAR(50) NULL COMMENT '手机号码',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户核心表';

-- 1.2 用户资料表
CREATE TABLE IF NOT EXISTS t_user_profile (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
    `nickname` VARCHAR(100) NULL COMMENT '用户昵称',
    `avatar_url` VARCHAR(512) NULL COMMENT '用户头像图片的URL链接',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户资料表 (1:1)';

-- 1.3 绑定设备表
CREATE TABLE IF NOT EXISTS t_bound_device (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
    `device_type` VARCHAR(50) NOT NULL COMMENT '设备类型 (ios, android, web)',
    `device_token` VARCHAR(512) NOT NULL COMMENT '设备的推送令牌',
    `last_login_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_device` (`user_id`, `device_token`(255))
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='绑定设备表';

-- 1.4 数据授权表
CREATE TABLE IF NOT EXISTS t_data_authorization (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
    `data_type` VARCHAR(100) NOT NULL COMMENT '授权的数据类型 (healthkit, google_calendar)',
    `is_authorized` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '用户是否已授权',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_data_type` (`user_id`, `data_type`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='数据授权表';

-- 1.5 用户画像指标表 (V2)
CREATE TABLE IF NOT EXISTS t_user_persona_metric (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
    `metric_name` VARCHAR(100) NOT NULL COMMENT '指标名称 (Key)',
    `metric_value` VARCHAR(255) NOT NULL COMMENT '指标的值 (Value)',
    `last_updated_by` VARCHAR(50) NOT NULL COMMENT '最后更新来源 (ai_service, user_input)',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '该指标的最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_metric` (`user_id`, `metric_name`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户画像指标表 (EAV模式)';

-- 1.6 画像标签(Lookup)
CREATE TABLE IF NOT EXISTS t_persona_tag (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签唯一主键',
    `tag_name` VARCHAR(100) NOT NULL COMMENT '标签名称 (学生, 拖延, 早睡者)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='画像标签(Lookup)';

-- 1.7 用户-画像标签-映射表
CREATE TABLE IF NOT EXISTS t_user_persona_tag_map (
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
    `tag_id` BIGINT NOT NULL COMMENT '关联的标签ID',
    PRIMARY KEY (`user_id`, `tag_id`),
    FOREIGN KEY (`user_id`) REFERENCES `t_user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `t_persona_tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='用户-画像标签-映射表 (M:N)';


-- -----------------------------------------------------------
-- 2. 日程服务 (schedule_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS schedule_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE schedule_service_db;

-- 2.1 日程类型表 (Lookup)
CREATE TABLE IF NOT EXISTS t_schedule_type (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '类型的唯一主键',
    `user_id` BIGINT NULL COMMENT '关联的用户ID (NULL为系统预设)',
    `type_name` VARCHAR(100) NOT NULL COMMENT '类型名称 (工作, 生活)',
    `color_hex` VARCHAR(10) NOT NULL COMMENT '前端显示的颜色代码',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程类型表 (Lookup)';

-- 2.2 日程标签表 (Lookup)
CREATE TABLE IF NOT EXISTS t_schedule_tag (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签的唯一主键',
    `tag_name` VARCHAR(100) NOT NULL COMMENT '标签名称',
    `created_by` VARCHAR(50) NOT NULL COMMENT '创建来源 (user, ai)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`tag_name`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程标签表 (Lookup)';

-- 2.3 日程主表 (V2)
CREATE TABLE IF NOT EXISTS t_schedule (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日程的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `title` VARCHAR(255) NOT NULL COMMENT '日程标题',
    `description` TEXT NULL COMMENT '日程的详细描述',
    `start_time` DATETIME NOT NULL COMMENT '日程开始时间 (重复事件的首次开始时间)',
    `end_time` DATETIME NOT NULL COMMENT '日程结束时间',
    `type_id` BIGINT NULL COMMENT '关联的日程类型ID',
    `is_urgent` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否紧急',
    `is_important` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否重要',
    `recurrence_rule` VARCHAR(255) NULL COMMENT 'iCal RRULE重复规则字符串',
    `status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '日程状态 (pending, completed)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_start_time` (`start_time`),
    FOREIGN KEY (`type_id`) REFERENCES `t_schedule_type`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程主表';

-- 2.4 日程例外表 (V2)
CREATE TABLE IF NOT EXISTS t_schedule_exception (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '例外规则的唯一主键',
    `schedule_id` BIGINT NOT NULL COMMENT '关联的主日程ID',
    `original_start_time` DATETIME NOT NULL COMMENT '要覆盖的原始时间点',
    `is_cancelled` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否仅取消这一次',
    `new_title` VARCHAR(255) NULL COMMENT '可修改单次标题',
    `new_start_time` DATETIME NULL COMMENT '修改后的单次开始时间',
    `new_end_time` DATETIME NULL COMMENT '修改后的单次结束时间',
    PRIMARY KEY (`id`),
    INDEX `idx_schedule_id` (`schedule_id`),
    UNIQUE KEY `uk_schedule_original_time` (`schedule_id`, `original_start_time`),
    FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程例外表 (用于重复日程的调整)';

-- 2.5 日程-标签-映射表
CREATE TABLE IF NOT EXISTS t_schedule_tag_map (
    `schedule_id` BIGINT NOT NULL COMMENT '关联的日程ID',
    `tag_id` BIGINT NOT NULL COMMENT '关联的标签ID',
    PRIMARY KEY (`schedule_id`, `tag_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `t_schedule_tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程-标签-映射表 (M:N)';

-- 2.6 日程提醒表
CREATE TABLE IF NOT EXISTS t_schedule_reminder (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '提醒的唯一主键',
    `schedule_id` BIGINT NOT NULL COMMENT '关联的日程ID',
    `remind_at` DATETIME NOT NULL COMMENT '提醒任务触发的绝对时间点',
    `status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '提醒的状态 (pending, sent, failed)',
    PRIMARY KEY (`id`),
    INDEX `idx_remind_at_status` (`remind_at`, `status`),
    FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='日程提醒表';

-- 2.7 AI建议存储表
CREATE TABLE IF NOT EXISTS t_schedule_ai_suggestion (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '建议的唯一主键',
    `schedule_id` BIGINT NOT NULL COMMENT '关联的日程ID',
    `suggestion_type` VARCHAR(100) NOT NULL COMMENT '建议类型 (time_conflict)',
    `suggestion_content` TEXT NOT NULL COMMENT '建议的具体内容',
    `is_accepted` BOOLEAN NULL COMMENT '用户是否接受 (NULL表示未操作)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_schedule_id` (`schedule_id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='AI建议存储表';

-- 2.8 天气缓存表
CREATE TABLE IF NOT EXISTS t_weather_cache (
    `cache_key` VARCHAR(255) NOT NULL COMMENT '缓存键 (location_date)',
    `data_json` JSON NOT NULL COMMENT '存储的天气数据 (JSON格式)',
    `expires_at` DATETIME NOT NULL COMMENT '缓存的过期时间',
    PRIMARY KEY (`cache_key`),
    INDEX `idx_expires_at` (`expires_at`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='天气缓存表';


-- -----------------------------------------------------------
-- 3. 财务服务 (finance_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS finance_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE finance_service_db;

-- 3.1 财务分类表 (Lookup)
CREATE TABLE IF NOT EXISTS t_finance_category (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类的唯一主键',
    `parent_id` BIGINT NULL COMMENT '父分类ID (用于多级分类)',
    `user_id` BIGINT NULL COMMENT '关联的用户ID (NULL为系统预设)',
    `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称 (餐饮, 交通)',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`parent_id`) REFERENCES `t_finance_category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='财务分类表 (Lookup)';

-- 3.2 交易流水表 (V2)
CREATE TABLE IF NOT EXISTS t_transaction (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '交易流水的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `amount` DECIMAL(10, 2) NOT NULL COMMENT '交易金额',
    `type` VARCHAR(50) NOT NULL COMMENT '交易类型 (income, expense)',
    `transaction_date` DATETIME NOT NULL COMMENT '交易发生的时间',
    `description` TEXT NULL COMMENT '交易描述',
    `category_id` BIGINT NULL COMMENT '关联的财务分类ID',
    `currency_code` VARCHAR(3) NOT NULL DEFAULT 'CNY' COMMENT '货币代码 (CNY, USD)',
    `ai_suggestion_json` JSON NULL COMMENT '存储AI的建议 (置信度等)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_transaction_date` (`transaction_date`),
    FOREIGN KEY (`category_id`) REFERENCES `t_finance_category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='交易流水表';

-- 3.3 预算表
CREATE TABLE IF NOT EXISTS t_budget (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预算的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `category_id` BIGINT NULL COMMENT '关联的财务分类ID (NULL为总预算)',
    `amount` DECIMAL(10, 2) NOT NULL COMMENT '预算金额',
    `budget_month` INT NOT NULL COMMENT '预算对应的月份 (1-12)',
    `budget_year` INT NOT NULL COMMENT '预算对应的年份',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_category_month` (`user_id`, `category_id`, `budget_year`, `budget_month`),
    FOREIGN KEY (`category_id`) REFERENCES `t_finance_category`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='预算表';


-- -----------------------------------------------------------
-- 4. 饮食记录服务 (diet_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS diet_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE diet_service_db;

-- 4.1 食物库 (V2)
CREATE TABLE IF NOT EXISTS t_food_item (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '食物的唯一主键',
    `food_name` VARCHAR(255) NOT NULL COMMENT '食物名称',
    `calories_per_100g` DECIMAL(10, 2) NOT NULL COMMENT '每100克的卡路里 (kcal)',
    `protein_per_100g` DECIMAL(10, 2) NULL COMMENT '每100克的蛋白质 (g)',
    `fat_per_100g` DECIMAL(10, 2) NULL COMMENT '每100克的脂肪 (g)',
    `carbs_per_100g` DECIMAL(10, 2) NULL COMMENT '每100克的碳水化合物 (g)',
    `created_by` VARCHAR(50) NOT NULL COMMENT '创建来源 (system, user)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_food_name` (`food_name`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='食物库';

-- 4.2 饮食日志(餐次)表
CREATE TABLE IF NOT EXISTS t_diet_log (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '饮食日志(餐次)的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `meal_type` VARCHAR(50) NOT NULL COMMENT '餐次类型 (breakfast, lunch, dinner, snack)',
    `log_time` DATETIME NOT NULL COMMENT '记录该餐次的时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_log_time` (`user_id`, `log_time`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='饮食日志(餐次)表';

-- 4.3 饮食条目表 (V2)
CREATE TABLE IF NOT EXISTS t_diet_log_entry (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '单条食物记录的主键',
    `log_id` BIGINT NOT NULL COMMENT '关联的餐次ID',
    `image_url` VARCHAR(512) NULL COMMENT '用户上传的食物图片URL',
    `food_item_id` BIGINT NULL COMMENT '关联到食物库的ID',
    `custom_food_name` VARCHAR(255) NULL COMMENT '如果食物库没有, 用户自定义名称',
    `quantity_g` DECIMAL(10, 2) NULL COMMENT '食用重量(克)',
    `quantity_desc` VARCHAR(100) NULL COMMENT '食用描述 (1个, 半碗)',
    `calories_kcal` DECIMAL(10, 2) NULL COMMENT '冗余存储该条目的总卡路里',
    PRIMARY KEY (`id`),
    INDEX `idx_log_id` (`log_id`),
    FOREIGN KEY (`log_id`) REFERENCES `t_diet_log`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`food_item_id`) REFERENCES `t_food_item`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='饮食条目表';


-- -----------------------------------------------------------
-- 5. 学习服务 (study_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS study_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE study_service_db;

-- 5.1 学习计划表
CREATE TABLE IF NOT EXISTS t_study_plan (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学习计划的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `title` VARCHAR(255) NOT NULL COMMENT '计划标题',
    `goal` TEXT NULL COMMENT '计划的最终目标描述',
    `start_date` DATE NULL COMMENT '计划开始日期',
    `end_date` DATE NULL COMMENT '计划结束日期',
    `status` VARCHAR(50) NOT NULL COMMENT '计划状态 (active, completed, archived)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='学习计划表';

-- 5.2 学习任务表
CREATE TABLE IF NOT EXISTS t_study_task (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学习任务的唯一主键',
    `plan_id` BIGINT NULL COMMENT '关联的学习计划ID (NULL为独立任务)',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `title` VARCHAR(255) NOT NULL COMMENT '任务标题',
    `is_completed` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '任务是否已完成',
    `due_date` DATE NULL COMMENT '任务的截止日期',
    PRIMARY KEY (`id`),
    INDEX `idx_plan_id` (`plan_id`),
    INDEX `idx_user_id` (`user_id`),
    FOREIGN KEY (`plan_id`) REFERENCES `t_study_plan`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='学习任务表';

-- 5.3 专注记录表
CREATE TABLE IF NOT EXISTS t_focus_session (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '专注时段的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `start_time` DATETIME NOT NULL COMMENT '专注开始的时间',
    `duration_minutes` INT NOT NULL COMMENT '实际专注的时长(分钟)',
    `status` VARCHAR(50) NOT NULL COMMENT '专注状态 (completed, interrupted)',
    `related_task_id` BIGINT NULL COMMENT '关联的学习任务ID',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_start_time` (`start_time`),
    FOREIGN KEY (`related_task_id`) REFERENCES `t_study_task`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='专注记录表 (番茄钟)';


-- -----------------------------------------------------------
-- 6. AI分析报告服务 (ai_analysis_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS ai_analysis_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE ai_analysis_service_db;

-- 6.1 分析报告存储表
CREATE TABLE IF NOT EXISTS t_generated_report (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '报告的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `report_type` VARCHAR(50) NOT NULL COMMENT '报告类型 (daily, weekly, monthly)',
    `start_date` DATE NOT NULL COMMENT '报告所涵盖的开始日期',
    `end_date` DATE NOT NULL COMMENT '报告所涵盖的结束日期',
    `report_data_json` JSON NOT NULL COMMENT '报告的详细内容 (JSON格式)',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_report_date` (`user_id`, `report_type`, `start_date`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='分析报告存储表';

-- 6.2 每日聚合指标表
CREATE TABLE IF NOT EXISTS t_agg_daily_metrics (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `date` DATE NOT NULL COMMENT '指标对应的日期',
    `total_calories` DECIMAL(10, 2) NULL COMMENT '当日总摄入卡路里',
    `total_focus_minutes` INT NULL COMMENT '当日总专注分钟',
    `total_expense` DECIMAL(10, 2) NULL COMMENT '当日总支出',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `date`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='每日聚合指标表 (ETL结果)';

-- 6.3 AI模型信息表
CREATE TABLE IF NOT EXISTS t_ai_model_info (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模型的唯一主键',
    `model_name` VARCHAR(100) NOT NULL COMMENT 'AI模型的名称',
    `version` VARCHAR(50) NOT NULL COMMENT '模型的版本号',
    `description` TEXT NULL COMMENT '模型描述',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_model_version` (`model_name`, `version`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='AI模型信息表';


-- -----------------------------------------------------------
-- 7. 通知服务 (notification_service_db)
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS notification_service_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE notification_service_db;

-- 7.1 通知发送日志
CREATE TABLE IF NOT EXISTS t_notification_log (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '接收通知的用户ID (非外键)',
    `content` TEXT NOT NULL COMMENT '通知的具体内容',
    `channel` VARCHAR(50) NOT NULL COMMENT '实际发送的渠道 (push, websocket, sms)',
    `status` VARCHAR(50) NOT NULL COMMENT '发送状态 (sent, failed, read)',
    `source_event_type` VARCHAR(100) NULL COMMENT '触发此通知的源事件类型',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '通知创建的时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='通知发送日志';

-- 7.2 通知偏好表 (V2)
CREATE TABLE IF NOT EXISTS t_notification_preference (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '偏好设置的唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `event_type` VARCHAR(100) NOT NULL COMMENT '事件类型 (schedule_reminder)',
    `channel` VARCHAR(50) NOT NULL COMMENT '通知渠道 (push, websocket)',
    `alert_type` VARCHAR(100) NOT NULL COMMENT '提醒方式 (sound_and_vibration, silent)',
    `is_enabled` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否启用此条偏好',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_event_channel` (`user_id`, `event_type`, `channel`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='通知偏好表';

-- 7.3 免打扰(DND)设置
CREATE TABLE IF NOT EXISTS t_notification_dnd (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
    `user_id` BIGINT NOT NULL COMMENT '关联的用户ID (非外键)',
    `is_enabled` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否开启免打扰模式',
    `start_time` TIME NULL COMMENT '免打扰开始时间 (时:分:秒)',
    `end_time` TIME NULL COMMENT '免打扰结束时间 (时:分:秒)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB CHARSET=utf8mb4 COMMENT='免打扰(DND)设置';

-- -----------------------------------------------------------
-- 脚本执行结束

-- -----------------------------------------------------------