-- =============================================
-- JavaForm IT运维管理平台 - 数据库初始化脚本
-- =============================================

-- 创建 Nacos 配置库
CREATE DATABASE IF NOT EXISTS nacos_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS zx_user_service_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建业务服务数据库
CREATE DATABASE IF NOT EXISTS zx_top_service_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建消息服务数据库
CREATE DATABASE IF NOT EXISTS zx_msg_service_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 用户服务数据库表
-- =============================================
USE zx_user_service_db;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `bio` VARCHAR(500) COMMENT '个人简介',
    `institution_id` VARCHAR(64) COMMENT '所属机构ID',
    `description` VARCHAR(500) COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE-正常, DISABLED-禁用',
    `create_by` VARCHAR(64) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_institution_id` (`institution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_name` VARCHAR(100) NOT NULL COMMENT '权限名称',
    `description` VARCHAR(500) COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 组织架构表
CREATE TABLE IF NOT EXISTS `group` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `group_name` VARCHAR(100) NOT NULL COMMENT '组织名称',
    `parent_id` VARCHAR(64) COMMENT '父组织ID',
    `institution_id` VARCHAR(64) COMMENT '机构ID',
    `description` VARCHAR(500) COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

-- 用户-权限关联表
CREATE TABLE IF NOT EXISTS `permission_and_user` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `permission_id` VARCHAR(64) NOT NULL COMMENT '权限ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_permission` (`user_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限关联表';

-- 用户-组织关联表
CREATE TABLE IF NOT EXISTS `group_and_user` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `group_id` VARCHAR(64) NOT NULL COMMENT '组织ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_group` (`user_id`, `group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组织关联表';

-- 初始化管理员用户 (密码: admin123)
INSERT INTO `user` (`id`, `username`, `password`, `email`, `status`) VALUES
('1', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@javaform.com', 'ACTIVE')
ON DUPLICATE KEY UPDATE username = username;

-- 初始化权限
INSERT INTO `permission` (`id`, `permission_code`, `permission_name`) VALUES
('1', 'user:read', '查看用户'),
('2', 'user:write', '创建用户'),
('3', 'user:update', '更新用户'),
('4', 'user:delete', '删除用户'),
('5', 'admin:development_record_control', '管理开发记录'),
('6', 'ticket:handle', '处理工单'),
('7', 'asset:manage', '管理资产'),
('8', 'approval:approve', '审批权限')
ON DUPLICATE KEY UPDATE permission_code = permission_code;

-- =============================================
-- 业务服务数据库表
-- =============================================
USE zx_top_service_db;

-- 开发记录表
CREATE TABLE IF NOT EXISTS `development_record` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `record_content` TEXT NOT NULL COMMENT '记录内容',
    `user_id` VARCHAR(64) COMMENT '用户ID',
    `institution_id` VARCHAR(64) COMMENT '机构ID',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `create_by` VARCHAR(64) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开发记录表';

-- IT工单表
CREATE TABLE IF NOT EXISTS `it_ticket` (
    `id` VARCHAR(64) NOT NULL COMMENT '工单ID',
    `title` VARCHAR(200) NOT NULL COMMENT '工单标题',
    `description` TEXT COMMENT '工单描述',
    `ticket_type` VARCHAR(20) COMMENT '工单类型: HARDWARE-硬件, SOFTWARE-软件, NETWORK-网络, ACCOUNT-账号, OTHER-其他',
    `priority` VARCHAR(20) DEFAULT 'MEDIUM' COMMENT '优先级: LOW-低, MEDIUM-中, HIGH-高, URGENT-紧急',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭',
    `applicant_id` VARCHAR(64) COMMENT '申请人ID',
    `applicant_name` VARCHAR(100) COMMENT '申请人姓名',
    `handler_id` VARCHAR(64) COMMENT '处理人ID',
    `handler_name` VARCHAR(100) COMMENT '处理人姓名',
    `institution_id` VARCHAR(64) COMMENT '所属机构ID',
    `solution` TEXT COMMENT '解决方案',
    `remark` VARCHAR(500) COMMENT '备注',
    `expected_time` DATETIME COMMENT '期望完成时间',
    `completed_time` DATETIME COMMENT '实际完成时间',
    `create_by` VARCHAR(64) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_applicant_id` (`applicant_id`),
    KEY `idx_handler_id` (`handler_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IT工单表';

-- IT资产表
CREATE TABLE IF NOT EXISTS `it_asset` (
    `id` VARCHAR(64) NOT NULL COMMENT '资产ID',
    `asset_code` VARCHAR(50) NOT NULL COMMENT '资产编号',
    `asset_name` VARCHAR(100) NOT NULL COMMENT '资产名称',
    `asset_type` VARCHAR(20) COMMENT '资产类型: COMPUTER-电脑, MONITOR-显示器, PRINTER-打印机, NETWORK-网络设备, SERVER-服务器',
    `brand` VARCHAR(50) COMMENT '品牌',
    `model` VARCHAR(100) COMMENT '型号',
    `serial_number` VARCHAR(100) COMMENT '序列号',
    `status` VARCHAR(20) DEFAULT 'AVAILABLE' COMMENT '状态: AVAILABLE-可用, IN_USE-使用中, REPAIRING-维修中, SCRAPPED-已报废',
    `user_id` VARCHAR(64) COMMENT '使用人ID',
    `user_name` VARCHAR(100) COMMENT '使用人姓名',
    `institution_id` VARCHAR(64) COMMENT '所属机构ID',
    `location` VARCHAR(200) COMMENT '存放位置',
    `purchase_date` DATE COMMENT '购买日期',
    `purchase_amount` DECIMAL(10,2) COMMENT '购买金额',
    `warranty_expiry` DATE COMMENT '保修截止日期',
    `configuration` TEXT COMMENT '配置信息(JSON)',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_by` VARCHAR(64) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_asset_code` (`asset_code`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IT资产表';

-- 审批流程表
CREATE TABLE IF NOT EXISTS `it_approval` (
    `id` VARCHAR(64) NOT NULL COMMENT '审批ID',
    `ticket_id` VARCHAR(64) COMMENT '关联工单ID',
    `approval_type` VARCHAR(30) COMMENT '审批类型: TICKET_HANDLE-工单处理, ASSET_APPLY-资产申请, ASSET_RETURN-资产归还',
    `title` VARCHAR(200) NOT NULL COMMENT '审批标题',
    `content` TEXT COMMENT '审批内容',
    `applicant_id` VARCHAR(64) COMMENT '申请人ID',
    `applicant_name` VARCHAR(100) COMMENT '申请人姓名',
    `approver_id` VARCHAR(64) COMMENT '审批人ID',
    `approver_name` VARCHAR(100) COMMENT '审批人姓名',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝, CANCELLED-已取消',
    `opinion` VARCHAR(500) COMMENT '审批意见',
    `approval_time` DATETIME COMMENT '审批时间',
    `institution_id` VARCHAR(64) COMMENT '所属机构ID',
    `create_by` VARCHAR(64) COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_ticket_id` (`ticket_id`),
    KEY `idx_applicant_id` (`applicant_id`),
    KEY `idx_approver_id` (`approver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程表';

-- =============================================
-- 消息服务数据库表
-- =============================================
USE zx_msg_service_db;

-- 消息模板表
CREATE TABLE IF NOT EXISTS `template` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `template_code` VARCHAR(50) NOT NULL COMMENT '模板编码',
    `template_content` TEXT NOT NULL COMMENT '模板内容',
    `carrier_type` VARCHAR(20) COMMENT '载体类型: EMAIL-邮件, SMS-短信',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- 消息记录表
CREATE TABLE IF NOT EXISTS `message` (
    `id` VARCHAR(64) NOT NULL COMMENT '主键',
    `template_id` VARCHAR(64) COMMENT '模板ID',
    `carrier_type` VARCHAR(20) COMMENT '载体类型',
    `receiver` VARCHAR(200) NOT NULL COMMENT '接收人',
    `subject` VARCHAR(200) COMMENT '主题',
    `content` TEXT COMMENT '内容',
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待发送, SENT-已发送, FAILED-发送失败',
    `send_time` DATETIME COMMENT '发送时间',
    `error_msg` VARCHAR(500) COMMENT '错误信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_receiver` (`receiver`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息记录表';

-- 初始化邮件模板
INSERT INTO `template` (`id`, `template_name`, `template_code`, `template_content`, `carrier_type`) VALUES
('1', '验证码模板', 'VERIFICATION_CODE', '您的验证码是: ${code}, 有效期5分钟。', 'EMAIL'),
('2', '工单通知模板', 'TICKET_NOTIFY', '尊敬的${username}, 您的工单${ticketTitle}状态已更新为${status}。', 'EMAIL'),
('3', '审批通知模板', 'APPROVAL_NOTIFY', '尊敬的${username}, 您有一个待审批的申请: ${title}。', 'EMAIL')
ON DUPLICATE KEY UPDATE template_code = template_code;
