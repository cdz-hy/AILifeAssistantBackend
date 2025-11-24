CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(36) PRIMARY KEY,
  username VARCHAR(64) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  nickname VARCHAR(64),
  avatar_url VARCHAR(255),
  consent_health_analysis TINYINT(1) DEFAULT 0,
  consent_finance_analysis TINYINT(1) DEFAULT 0,
  persona_json TEXT NULL
);

CREATE TABLE IF NOT EXISTS user_device (
  user_id VARCHAR(36) NOT NULL,
  device_id VARCHAR(128) NOT NULL,
  PRIMARY KEY (user_id, device_id),
  CONSTRAINT fk_user_device_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);