-- =============================================
-- JavaForm IT运维管理平台 - ClickHouse 初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS http_log_db;

USE http_log_db;

-- HTTP日志表
CREATE TABLE IF NOT EXISTS http_log (
    id String,
    request_id String,
    service_name String,
    client_ip String,
    server_ip String,
    request_method String,
    request_uri String,
    request_query String,
    request_headers String,
    request_body String,
    response_status Int32,
    response_headers String,
    response_body String,
    log_level String,
    duration Int64,
    create_time DateTime,
    INDEX idx_client_ip client_ip TYPE bloom_filter GRANULARITY 1,
    INDEX idx_service_name service_name TYPE bloom_filter GRANULARITY 1,
    INDEX idx_request_uri request_uri TYPE bloom_filter GRANULARITY 1
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(create_time)
ORDER BY (create_time, service_name, request_id)
TTL create_time + INTERVAL 90 DAY;

-- 访问统计表
CREATE TABLE IF NOT EXISTS visit_statistics (
    date Date,
    hour UInt8,
    service_name String,
    uri String,
    request_count UInt64,
    avg_duration Float64,
    error_count UInt64
) ENGINE = SummingMergeTree()
PARTITION BY toYYYYMM(date)
ORDER BY (date, hour, service_name, uri);

-- 高频IP统计表
CREATE TABLE IF NOT EXISTS high_frequency_ip (
    date Date,
    client_ip String,
    request_count UInt64,
    avg_interval Float64,
    service_name String
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(date)
ORDER BY (date, client_ip);
