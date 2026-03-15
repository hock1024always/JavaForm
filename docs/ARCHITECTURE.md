# JavaForm IT运维管理平台 - 软件工程文档

## 1. 系统概述

### 1.1 项目背景
JavaForm IT运维管理平台是一个面向企业IT部门的管理系统，旨在提高IT运维效率，实现工单管理、资产管理、审批流程的数字化管理。

### 1.2 系统目标
- 提供统一的IT工单管理入口，规范工单处理流程
- 实现IT资产的全生命周期管理
- 支持灵活的审批流程配置
- 提供完善的日志审计能力

### 1.3 技术架构
采用微服务架构，基于Spring Cloud Alibaba生态，实现服务注册发现、配置管理、API网关等核心能力。

---

## 2. 系统架构

### 2.1 整体架构

```
┌────────────────────────────────────────────────────────────────────────┐
│                           客户端层                                       │
│                    (Web Browser / Mobile App)                           │
└────────────────────────────────┬───────────────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          API网关层 (zx_gateway)                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────┐ │
│  │  路由转发    │  │  JWT认证     │  │  限流熔断    │  │  日志记录  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘  └────────────┘ │
└────────────────────────────────┬───────────────────────────────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
        ▼                        ▼                        ▼
┌───────────────┐      ┌───────────────┐      ┌───────────────┐
│ zx_userservice│      │  zx_topbiz    │      │zx_msgservice  │
│   用户服务     │      │  业务聚合服务  │      │   消息服务    │
│   Port: 8081  │      │   Port: 8082  │      │   Port: 8083  │
└───────┬───────┘      └───────┬───────┘      └───────┬───────┘
        │                      │                      │
        └──────────────────────┼──────────────────────┘
                               │
                               ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         基础设施层                                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │  MySQL   │  │  Redis   │  │  Nacos   │  │ClickHouse│  │  Mail    │ │
│  │ 业务存储  │  │ 缓存会话  │  │ 注册配置  │  │ 日志存储  │  │ 邮件服务  │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└────────────────────────────────────────────────────────────────────────┘
```

### 2.2 服务依赖关系

```
zx_gateway
    ├── zx_userservice (Feign调用)
    ├── zx_topbiz (路由转发)
    ├── zx_msgservice (Feign调用)
    └── zx_logservice (路由转发)

zx_topbiz
    ├── zx_userservice (Feign调用 - 用户信息)
    ├── zx_msgservice (Feign调用 - 消息通知)
    └── zx_logservice (Feign调用 - 日志记录)
```

---

## 3. 模块详细设计

### 3.1 用户服务 (zx_userservice)

#### 3.1.1 功能说明
用户服务负责用户认证、权限管理、组织架构管理等核心用户相关功能。

#### 3.1.2 核心接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /user/user/loginwithnameandpassword | POST | 用户名密码登录 | name, password | ResultVO(含token) |
| /user/user/loginwithemailandpassword | POST | 邮箱密码登录 | email, password | ResultVO(含token) |
| /user/user/generateverificationcode | POST | 生成验证码 | email | 验证码发送结果 |
| /user/user/checkverificationcode | POST | 验证验证码 | email, verificationCode | ResultVO |
| /user/user/userregister | POST | 用户注册 | email, username, password | ResultVO |
| /user/user/userlogout | POST | 用户登出 | username | ResultVO |
| /user/user/checkstatus | POST | 检查登录状态 | username | ResultVO |
| /user/user/cancel | POST | 注销账号 | id | ResultVO |

#### 3.1.3 数据模型

**用户表 (user)**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键 |
| username | VARCHAR(50) | 用户名(唯一) |
| password | VARCHAR(255) | 密码(加密) |
| email | VARCHAR(100) | 邮箱(唯一) |
| phone | VARCHAR(20) | 手机号 |
| status | VARCHAR(20) | 状态: ACTIVE/DISABLED |
| institution_id | VARCHAR(64) | 所属机构ID |

**权限表 (permission)**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键 |
| permission_code | VARCHAR(100) | 权限编码 |
| permission_name | VARCHAR(100) | 权限名称 |

**组织架构表 (group)**
| 字段 | 类型 | 说明 |
|------|------|------|
| id | VARCHAR(64) | 主键 |
| group_name | VARCHAR(100) | 组织名称 |
| parent_id | VARCHAR(64) | 父组织ID |

---

### 3.2 业务聚合服务 (zx_topbiz)

#### 3.2.1 功能说明
业务聚合服务是核心业务逻辑的实现层，包含IT工单管理、IT资产管理、审批流程等功能。

#### 3.2.2 工单管理接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /ticket/create | POST | 创建工单 | ItTicket对象 | ItTicket |
| /ticket/update | PUT | 更新工单 | ItTicket对象 | ItTicket |
| /ticket/assign | POST | 分配工单 | ticketId, handlerId, handlerName | ItTicket |
| /ticket/handle | POST | 处理工单 | ticketId, solution, status | ItTicket |
| /ticket/close | POST | 关闭工单 | ticketId, remark | ItTicket |
| /ticket/page | GET | 分页查询 | pageNum, pageSize, status等 | Page<ItTicket> |
| /ticket/{id} | GET | 查询详情 | id | ItTicket |
| /ticket/{id} | DELETE | 删除工单 | id | void |

**工单状态流转**
```
PENDING(待处理) → PROCESSING(处理中) → RESOLVED(已解决) → CLOSED(已关闭)
                              ↓
                         REJECTED(已拒绝)
```

**工单类型枚举**
| 类型 | 说明 |
|------|------|
| HARDWARE | 硬件问题 |
| SOFTWARE | 软件问题 |
| NETWORK | 网络问题 |
| ACCOUNT | 账号问题 |
| OTHER | 其他 |

**优先级枚举**
| 优先级 | 说明 |
|--------|------|
| LOW | 低 |
| MEDIUM | 中 |
| HIGH | 高 |
| URGENT | 紧急 |

#### 3.2.3 资产管理接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /asset/create | POST | 创建资产 | ItAsset对象 | ItAsset |
| /asset/update | PUT | 更新资产 | ItAsset对象 | ItAsset |
| /asset/assign | POST | 分配资产 | assetId, userId, userName | ItAsset |
| /asset/recycle | POST | 回收资产 | assetId | ItAsset |
| /asset/scrap | POST | 资产报废 | assetId, remark | ItAsset |
| /asset/page | GET | 分页查询 | pageNum, pageSize, status等 | Page<ItAsset> |
| /asset/{id} | GET | 查询详情 | id | ItAsset |
| /asset/code/{assetCode} | GET | 按编号查询 | assetCode | ItAsset |

**资产状态流转**
```
AVAILABLE(可用) → IN_USE(使用中) → AVAILABLE(回收)
       ↓                ↓
SCRAPPED(报废)   REPAIRING(维修中) → AVAILABLE
```

**资产类型枚举**
| 类型 | 说明 |
|------|------|
| COMPUTER | 电脑 |
| MONITOR | 显示器 |
| PRINTER | 打印机 |
| NETWORK | 网络设备 |
| SERVER | 服务器 |
| OTHER | 其他 |

#### 3.2.4 审批流程接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /approval/create | POST | 创建申请 | ItApproval对象 | ItApproval |
| /approval/approve | POST | 审批通过 | approvalId, opinion | ItApproval |
| /approval/reject | POST | 审批拒绝 | approvalId, opinion | ItApproval |
| /approval/cancel | POST | 取消申请 | approvalId | ItApproval |
| /approval/page | GET | 分页查询 | pageNum, pageSize, status等 | Page<ItApproval> |
| /approval/ticket/{ticketId} | GET | 按工单查询 | ticketId | ItApproval |

**审批状态流转**
```
PENDING(待审批) → APPROVED(已通过)
       ↓
  REJECTED(已拒绝)
       ↓
  CANCELLED(已取消)
```

---

### 3.3 消息服务 (zx_msgservice)

#### 3.3.1 功能说明
消息服务负责邮件发送、消息模板管理、定时任务调度等功能。

#### 3.3.2 核心接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /msg/send/instant | POST | 即时发送 | SendDTO | ResultVO |
| /msg/send/timing | POST | 定时发送 | SendDTO | ResultVO |
| /msg/send/period-send | POST | 周期发送 | SendDTO | ResultVO |

**SendDTO 结构**
```json
{
  "carrierId": "email",
  "templateId": "template_001",
  "templateParams": {
    "code": "123456"
  },
  "receivers": ["user@example.com"],
  "subject": "验证码通知",
  "timing": "2024-01-01 10:00:00",
  "cron": "0 0 9 * * ?"
}
```

---

### 3.4 日志服务 (zx_logservice)

#### 3.4.1 功能说明
日志服务负责HTTP请求日志的收集、存储和分析，使用ClickHouse作为存储引擎。

#### 3.4.2 核心接口

| 接口路径 | 方法 | 功能 | 参数 | 返回值 |
|---------|------|------|------|--------|
| /log/httplog/page-query | GET | 精确查询 | page, pageSize, clientIp等 | PageVO<HttpLog> |
| /log/httplog/page-search | GET | 模糊查询 | page, pageSize, requestUri等 | PageVO<HttpLog> |
| /log/http-statistics/num-visit | GET | 访问统计 | startTime, endTime | Map |
| /log/http-statistics/high-frequency-ip | GET | 高频IP | threshold, startTime | List |

---

### 3.5 API网关 (zx_gateway)

#### 3.5.1 功能说明
API网关是系统的统一入口，负责路由转发、JWT认证、跨域处理、API文档聚合等功能。

#### 3.5.2 路由配置

| 路由前缀 | 目标服务 | 说明 |
|---------|---------|------|
| /api/user/** | zx-userservice | 用户服务 |
| /api/permission/** | zx-userservice | 权限服务 |
| /api/group/** | zx-userservice | 组织架构 |
| /api/msg/** | zx-msgservice | 消息服务 |
| /api/log/** | zx-logservice | 日志服务 |
| /api/biz/** | zx-topbiz | 业务服务 |
| /api/ticket/** | zx-topbiz | 工单服务 |
| /api/asset/** | zx-topbiz | 资产服务 |

#### 3.5.3 认证流程

```
1. 客户端请求 → 网关接收
2. 检查是否白名单路径
   ├── 是 → 直接转发
   └── 否 → 检查Authorization头
3. 解析JWT Token
   ├── 有效 → 提取用户信息，添加到请求头转发
   └── 无效 → 返回401错误
4. 目标服务处理请求
```

**白名单路径**
- /api/user/user/loginwithnameandpassword
- /api/user/user/userregister
- /api/user/user/generateverificationcode
- /doc.html (API文档)
- /swagger-resources/**
- /v3/api-docs/**

---

## 4. 安全设计

### 4.1 认证机制
- 采用JWT Token认证
- Token有效期24小时
- Redis存储会话信息

### 4.2 权限控制
- 基于Shiro实现RBAC权限模型
- 支持用户-权限、用户-组织多维度授权
- 接口级别权限控制

### 4.3 数据安全
- 密码采用BCrypt加密存储
- 敏感配置使用环境变量
- 数据库连接使用SSL

---

## 5. 部署架构

### 5.1 Docker部署

```yaml
# 服务清单
services:
  - mysql (3306)
  - redis (6379)
  - nacos (8848)
  - clickhouse (8123/9000)
  - gateway (9000)
  - userservice (8081)
  - topbiz (8082)
  - msgservice (8083)
  - logservice (8084)
```

### 5.2 资源配置建议

| 服务 | CPU | 内存 | 说明 |
|------|-----|------|------|
| gateway | 1核 | 512MB | 可横向扩展 |
| userservice | 1核 | 512MB | 可横向扩展 |
| topbiz | 2核 | 1GB | 可横向扩展 |
| msgservice | 1核 | 512MB | 可横向扩展 |
| logservice | 1核 | 512MB | 可横向扩展 |
| mysql | 2核 | 2GB | 建议主从 |
| redis | 1核 | 512MB | 建议哨兵 |
| nacos | 1核 | 512MB | 建议集群 |

---

## 6. 监控与运维

### 6.1 健康检查
所有服务提供 /actuator/health 端点，支持Docker健康检查。

### 6.2 日志收集
- 业务日志: 标准输出，由Docker收集
- HTTP日志: ClickHouse存储，支持分析查询

### 6.3 性能指标
通过Spring Boot Actuator暴露指标，可对接Prometheus。

---

## 7. 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-01 | 初始版本，实现核心功能 |
