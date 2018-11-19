CREATE SCHEMA IF NOT EXISTS logs;
USE logs;
CREATE TABLE IF NOT EXISTS log
(
  id BIGINT NOT NULL AUTO_INCREMENT,
  log_date TIMESTAMP,
  ip VARCHAR(100),
  request VARCHAR(200),
  status int(3),
  user_agent VARCHAR(500),
  CONSTRAINT pk_log PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS blocked
(
  id BIGINT NOT NULL AUTO_INCREMENT,
  ip VARCHAR(100),
  reason VARCHAR(500),
  CONSTRAINT pk_log PRIMARY KEY(id)
);