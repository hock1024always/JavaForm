# JavaForm - IT运维管理平台

基于 **Spring Boot 3 + Spring Cloud Alibaba** 微服务架构的企业级 IT 运维管理平台，涵盖用户认证、工单管理、资产管理、审批流程、消息通知、日志审计六大核心域，支持 Docker Compose 一键部署。

---

## 目录

- [项目简介](#项目简介)
- [系统架构](#系统架构)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能模块详述](#功能模块详述)
- [快速开始](#快速开始)
- [全量接口清单](#全量接口清单)
- [数据库设计](#数据库设计)
- [配置参考](#配置参考)
- [常见问题](#常见问题)

---

## 项目简介

JavaForm 是一套面向企业 IT 部门的内部运维管理系统。核心目标：

1. **工单驱动** -- 员工在线提交故障/需求工单，运维人员在线受理、流转、闭环。
2. **资产全生命周期** -- 从采购入库、分配使用、维修到报废，全程记录和追溯。
3. **审批流程** -- 工单处理、资产申请/归还均可发起审批，审批人在线审批。
4. **消息通知** -- 工单状态变更、审批结果通过邮件即时/定时推送给相关人员。
5. **日志审计** -- 全链路 HTTP 日志采集存储在 ClickHouse，支持高频 IP 检测和访问链追踪。
6. **权限隔离** -- 基于 Shiro + JWT + Redis 的 RBAC 模型，支持接口级别权限控制。

---

## 系统架构

```
                          +--------------------------+
                          |    Client (Browser)      |
                          +-----------+--------------+
                                      |
                                      v
                   +------------------+------------------+
                   |     zx_gateway (Spring Cloud GW)    |
                   |          Port 9000                  |
                   |  - JWT 认证       - 路由转发         |
                   |  - CORS 跨域      - Knife4j 聚合     |
                   +------+--------+--------+------------+
                          |        |        |
              +-----------+--+  +--+--------+--+  +------+------+
              | zx_userservice|  |  zx_topbiz   |  |zx_msgservice|
              |  Port 8081    |  |  Port 8082   |  |  Port 8083  |
              |  用户/权限/组织 |  |  工单/资产/审批|  |  邮件/模板   |
              +-------+-------+  +------+-------+  +------+------+
                      |                 |                  |
                      +--------+--------+------------------+
                               |
         +----------+----------+-----------+-----------+
         |          |          |           |           |
     +---+---+ +---+---+ +----+----+ +----+----+ +----+----+
     | MySQL | | Redis | |  Nacos  | |ClickHouse| |  SMTP  |
     | 8.0   | | 7.x   | | 2.3.2  | |  latest  | |  Mail  |
     +-------+ +-------+ +---------+ +----------+ +--------+

         zx_logservice (Port 8084) 直接写入 ClickHouse
```

### 服务间调用

| 调用方 | 被调用方 | 协议 | 说明 |
|--------|---------|------|------|
| zx_gateway | 所有服务 | HTTP / Loadbalancer | 路由转发 + JWT 注入请求头 |
| zx_topbiz | zx_userservice | OpenFeign | 登录、注册、用户信息、权限绑定 |
| zx_topbiz | zx_msgservice | OpenFeign | 发送验证码邮件、业务通知 |
| zx_topbiz | zx_logservice | OpenFeign | 日志查询、统计分析 |

---

## 技术栈

### 后端框架

| 组件 | 版本 | 用途 |
|------|------|------|
| **Spring Boot** | 3.3.10 | 基础框架 |
| **Spring Cloud** | 2023.0.3 | 微服务体系 |
| **Spring Cloud Alibaba** | 2023.0.1.0 | Nacos 集成 |
| **Spring Cloud Gateway** | -- | API 网关 |
| **OpenFeign** | -- | 服务间远程调用 |
| **MyBatis-Plus** | 3.5.11 | ORM + 分页 |
| **MyBatis-Plus-Join** | 1.5.3 | 联表查询 |
| **Apache Shiro** | 2.0.0 (jakarta) | RBAC 权限 |
| **JWT (jjwt)** | 0.12.6 | Token 签发与校验 |
| **Knife4j** | 4.5.0 | OpenAPI 3 文档 |
| **Hutool** | 5.8.23 | 工具集 |
| **Logbook** | 3.1.0 | HTTP 日志拦截 |

### 数据存储

| 组件 | 版本 | 用途 |
|------|------|------|
| **MySQL** | 8.0 | 业务数据（用户、工单、资产、审批、消息模板） |
| **Redis** | 7.x | 会话缓存、验证码、权限缓存 |
| **ClickHouse** | latest | HTTP 请求日志、访问统计分析 |
| **Nacos** | 2.3.2 | 服务注册发现 + 配置中心 |

### 构建部署

| 组件 | 说明 |
|------|------|
| JDK 17 | 编译运行 |
| Maven 3.8+ | 构建工具 |
| Docker + Compose | 容器化一键部署 |

---

## 项目结构

```
JavaForm/
├── pom.xml                            # 父 POM，统一版本管理
├── docker-compose.yml                 # Docker 一键编排
├── Dockerfile                         # 多阶段镜像构建
├── .env.example                       # 环境变量模板
├── README.md
│
├── docs/
│   ├── ARCHITECTURE.md                # 架构设计文档
│   └── API.md                         # 接口详细文档
│
├── docker/
│   ├── mysql/init/01_init.sql         # MySQL 建库建表 + 初始数据
│   └── clickhouse/init/01_init.sql    # ClickHouse 日志表
│
├── zx_common/                         # [公共模块] 无独立启动
│   └── src/main/java/com/zxzy/common/
│       ├── domain/
│       │   ├── vo/                    # ResultVO / PageVO
│       │   └── enums/                 # ResultCode 状态码枚举
│       ├── exception/                 # BusinessException + GlobalExceptionHandler
│       └── utils/                     # SnowflakeIdUtil 等
│
├── zx_gateway/                        # [API 网关] Port 9000
│   └── src/main/java/com/zxzy/gateway/
│       ├── GatewayApplication.java
│       └── filter/
│           └── AuthGlobalFilter.java  # JWT 全局认证过滤器
│
├── zx_userservice/                    # [用户服务] Port 8081
│   └── src/main/java/com/zxzy/zx_userservice/
│       ├── controller/                # 7 个 Controller
│       │   ├── UserController.java         # 登录/注册/登出
│       │   ├── GroupController.java        # 组织架构 CRUD
│       │   ├── GroupAndUserController.java # 用户-组织关联
│       │   ├── GroupAndGroupController.java# 组织-组织关联
│       │   ├── PermissionController.java   # 权限 CRUD
│       │   ├── PermissionAndUserController.java
│       │   └── PermissionAndGroupController.java
│       ├── service/                   # 接口 + impl 实现
│       ├── mapper/                    # MyBatis-Plus Mapper
│       ├── domain/                    # PO / VO / DTO
│       ├── utils/                     # JWTUtils / SnowflakeIdUtil
│       ├── config/                    # MyBatis 配置
│       └── exception/                 # 异常处理
│
├── zx_topbiz/                         # [业务聚合服务] Port 8082
│   └── src/main/java/com/homework/topbiz/
│       ├── controller/                # 10 个 Controller
│       │   ├── AuthController.java         # 权限 Demo 登录
│       │   ├── UserController.java         # 用户管理聚合
│       │   ├── PermissionController.java   # 权限管理聚合
│       │   ├── GroupController.java        # 组织管理聚合
│       │   ├── DevelopmentRecordController.java # 开发记录
│       │   ├── HttpLogController.java      # 日志查询代理
│       │   ├── ItTicketController.java     # IT 工单管理
│       │   ├── ItAssetController.java      # IT 资产管理
│       │   ├── ItApprovalController.java   # 审批流程管理
│       │   └── TestController.java
│       ├── service/                   # 业务逻辑
│       ├── mapper/                    # 数据访问
│       ├── entity/                    # PO / VO / DTO
│       ├── api/                       # Feign 客户端
│       │   ├── UserClient.java        # -> zx_userservice
│       │   ├── MsgClient.java         # -> zx_msgservice
│       │   └── LogClient.java         # -> zx_logservice
│       ├── shiro/                     # CustomRealm / CustomFilter / CustomToken
│       ├── interceptor/               # HTTP 日志拦截器
│       ├── filter/                    # 请求缓存过滤器
│       ├── logger/                    # JSON 日志工具
│       └── config/                    # Shiro / Redis 配置
│
├── zx_msgservice/                     # [消息服务] Port 8083
│   └── src/main/java/com/homework/zx_msgservice/
│       ├── controller/
│       │   ├── SendController.java         # 即时/定时/周期发送
│       │   └── TemplateController.java     # 模板 CRUD
│       ├── service/                   # 发送/模板/定时任务
│       ├── mapper/
│       ├── domain/                    # Template / Message / MailConfig 等
│       ├── utils/                     # 模板渲染 / 载体工具
│       └── config/                    # Scheduler / MyBatis
│
└── zx_logservice/                     # [日志服务] Port 8084
    └── src/main/java/com/homework/zx_logservice/
        ├── controller/
        │   ├── HttpLogController.java      # 精确/模糊日志查询
        │   ├── HttpStatisticsController.java # 统计分析
        │   └── TestController.java
        ├── service/                   # 日志查询 + 统计
        ├── mapper/                    # ClickHouse Mapper
        ├── domain/                    # HttpLog / HttpLogDTO
        ├── interceptor/               # HTTP 请求日志拦截
        ├── filter/                    # 请求体缓存
        └── config/                    # WebMvc / MyBatis
```

---

## 功能模块详述

### 1. 用户服务 (zx_userservice)

| 功能域 | 说明 |
|--------|------|
| 认证 | 用户名+密码登录、邮箱+密码登录、邮箱+验证码登录 |
| 注册 | 邮箱注册，支持验证码校验 |
| 会话 | JWT Token 签发，Redis 存储会话状态 |
| 权限 | 权限 CRUD，权限-用户绑定/解绑 |
| 组织 | 树形组织架构 CRUD，用户-组织绑定/解绑 |
| 账号管理 | 检查登录状态、登出、注销账号 |

### 2. 业务聚合服务 (zx_topbiz)

#### 2a. IT 工单管理

完整的工单生命周期管理：

```
创建(PENDING) --> 分配处理人(PROCESSING) --> 处理完成(RESOLVED) --> 关闭(CLOSED)
                                         \-> 拒绝(REJECTED)
```

- **工单类型**: HARDWARE(硬件) / SOFTWARE(软件) / NETWORK(网络) / ACCOUNT(账号) / OTHER(其他)
- **优先级**: LOW / MEDIUM / HIGH / URGENT
- 支持按状态、类型、优先级、申请人、处理人筛选及关键词搜索

#### 2b. IT 资产管理

资产全生命周期管理：

```
登记(AVAILABLE) --> 分配给用户(IN_USE) --> 回收(AVAILABLE) --> 报废(SCRAPPED)
                                      \-> 送修(REPAIRING) --> 修好(AVAILABLE)
```

- **资产类型**: COMPUTER / MONITOR / PRINTER / NETWORK / SERVER / OTHER
- 唯一资产编号、品牌型号、序列号、购买金额、保修日期、配置信息(JSON)
- 支持按编号/名称/品牌模糊搜索

#### 2c. 审批流程

```
申请(PENDING) --> 通过(APPROVED)
             \-> 拒绝(REJECTED)
             \-> 取消(CANCELLED)
```

- **审批类型**: TICKET_HANDLE(工单处理) / ASSET_APPLY(资产申请) / ASSET_RETURN(资产归还) / OTHER
- 关联工单 ID，支持审批意见
- 支持按申请人、审批人筛选

#### 2d. 聚合能力

TopBiz 通过 OpenFeign 调用其他三个服务，充当 BFF(Backend for Frontend) 角色：
- 调用 UserClient 实现用户注册时自动绑定默认权限（区分员工/管理员角色）
- 调用 MsgClient 发送验证码邮件
- 调用 LogClient 代理日志查询和统计

#### 2e. 开发记录

Shiro 权限保护的开发记录 CRUD：管理员可查看全部记录，普通员工仅可查看自己的记录。

### 3. 消息服务 (zx_msgservice)

| 功能 | 说明 |
|------|------|
| 即时发送 | 立即发送邮件，支持模板填充 |
| 定时发送 | 指定时间发送 |
| 周期发送 | Cron 表达式驱动的周期性发送 |
| 模板管理 | 创建/查询/更新/删除邮件模板 |

预置模板：验证码模板、工单通知模板、审批通知模板。

### 4. 日志服务 (zx_logservice)

| 功能 | 说明 |
|------|------|
| 精确查询 | 按 IP / 服务名 / 日志级别 / 请求路径精确匹配 |
| 模糊搜索 | 按请求路径模糊匹配 |
| 访问统计 | 指定时间范围内的请求总数 |
| 高频 IP | 超过阈值的高频访问 IP 检测 |
| 访问链追踪 | 按 RequestId 还原完整调用链 |

ClickHouse 按月分区存储，90 天自动 TTL 清理。

### 5. API 网关 (zx_gateway)

| 功能 | 说明 |
|------|------|
| 路由转发 | 按 /api/{service}/** 规则转发到对应服务 |
| JWT 认证 | 解析 Token，注入 X-User-Id / X-Username 请求头 |
| 白名单 | 登录/注册/验证码/文档接口免认证 |
| 跨域 | 全局 CORS 配置 |
| API 文档 | Knife4j Gateway 自动聚合各服务 Swagger |

---

## 快速开始

### 环境要求

- **JDK** 17+
- **Maven** 3.8+
- **Docker** 20.10+ & **Docker Compose** v2

### 方式一：Docker Compose 一键部署 (推荐)

```bash
# 1. 进入项目目录
cd /home/hyz/AI/JavaForm

# 2. 从模板生成环境配置
cp .env.example .env
# 按需修改 .env 中的密码、邮件配置等

# 3. 启动全部服务（基础设施 + 微服务）
docker-compose up -d --build

# 4. 查看服务状态
docker-compose ps

# 5. 查看指定服务日志
docker-compose logs -f gateway
docker-compose logs -f userservice
```

> 首次启动需要拉取镜像和编译，耗时较长，后续启动很快。

#### 启动后可访问

| 地址 | 说明 |
|------|------|
| http://localhost:9000 | API 网关入口 |
| http://localhost:9000/doc.html | Knife4j API 文档 |
| http://localhost:8848/nacos | Nacos 控制台 (nacos/nacos) |

### 方式二：本地开发运行

```bash
# 1. 只启动基础设施
docker-compose up -d mysql redis nacos clickhouse

# 2. 等待基础设施就绪（约30秒）
docker-compose ps

# 3. 编译全部模块
mvn clean install -DskipTests

# 4. 依次启动各服务（开多个终端）

# 终端1 - 用户服务
java -jar zx_userservice/target/zx_userservice-1.0.0-SNAPSHOT.jar

# 终端2 - 消息服务
java -jar zx_msgservice/target/zx_msgservice-1.0.0-SNAPSHOT.jar

# 终端3 - 日志服务
java -jar zx_logservice/target/zx_logservice-1.0.0-SNAPSHOT.jar

# 终端4 - 业务聚合服务
java -jar zx_topbiz/target/zx_topbiz-1.0.0-SNAPSHOT.jar

# 终端5 - API 网关（最后启动）
java -jar zx_gateway/target/zx_gateway-1.0.0-SNAPSHOT.jar
```

### 方式三：IDEA 开发调试

1. 用 IDEA 打开项目根目录 `JavaForm/`，识别为 Maven 多模块项目。
2. 确保基础设施已启动（`docker-compose up -d mysql redis nacos clickhouse`）。
3. 分别运行各模块的 `Application.java` 启动类。
4. 修改代码后只需重启对应服务即可。

---

## 全量接口清单

> 所有接口通过网关访问时需加 `/api` 前缀。认证接口(标记[开放])无需 Token，其余需在请求头加 `Authorization: Bearer {token}`。

### 用户认证 (zx_userservice)

| 方法 | 路径 | 说明 | 备注 |
|------|------|------|------|
| POST | /user/user/loginwithnameandpassword | 用户名+密码登录 | [开放] |
| POST | /user/user/loginwithemailandpassword | 邮箱+密码登录 | [开放] |
| POST | /user/user/loginwithemailandVerificationCode | 邮箱+验证码登录 | [开放] |
| POST | /user/user/generateVerificationCode | 生成邮箱验证码 | [开放] |
| POST | /user/user/checkVerificationCode | 校验验证码 | [开放] |
| POST | /user/user/userregister | 用户注册 | [开放] |
| POST | /user/user/registerwithemailandverificationcode | 邮箱验证码注册 | [开放] |
| POST | /user/user/checkregister | 注册验证 | [开放] |
| POST | /user/user/userlogout | 登出 | |
| POST | /user/user/checkstatus | 检查登录状态 | |
| POST | /user/user/cancel | 注销账号 | |

### 聚合用户管理 (zx_topbiz)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/user/login-with-name-password | 用户名密码登录(聚合) | 开放 |
| POST | /api/user/send-verify-code | 发送验证码 | 开放 |
| POST | /api/user/login-with-name-verify-code | 验证码登录(聚合) | 开放 |
| POST | /api/user/userregister-emp | 员工注册(自动绑定权限) | 开放 |
| POST | /api/user/userregister-admin | 管理员注册 | 开放 |
| POST | /api/user/userlogout | 登出 | user:read |
| PUT | /api/user/update-self | 修改个人信息 | user:read |
| GET | /api/user/getusersinfo | 分页查询用户(管理员) | admin:user_control |
| POST | /api/user/user/cancel | 注销用户 | admin:user_control |
| POST | /api/login | 权限 Demo 登录 | 开放 |
| GET | /api/test | 权限测试接口 | user:read |

### 权限管理 (zx_topbiz -> zx_userservice)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/permission/create | 创建权限 | admin:permission_control |
| POST | /api/permission/delete | 删除权限 | admin:permission_control |
| GET | /api/permission/get | 获取权限列表 | admin:permission_control |
| POST | /api/permission/bind-user | 绑定权限-用户 | admin:permission_control |
| POST | /api/permission/unbind-user | 解绑权限-用户 | admin:permission_control |
| GET | /api/permission/selectByUserId | 查询用户权限 | admin:permission_control |

### 组织管理 (zx_topbiz -> zx_userservice)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/group/batch/create | 批量创建组织 | admin:group_control |
| DELETE | /api/group/batch/delete | 批量删除组织 | admin:group_control |
| PUT | /api/group/batch/update_name | 批量更新组织名 | admin:group_control |
| GET | /api/group/page/like_group_name | 模糊查询组织 | user:read |
| POST | /api/group/batch/binding | 绑定用户-组织 | admin:group_control |
| POST | /api/group/batch/unbind | 解绑用户-组织 | admin:group_control |
| PUT | /api/group/batch/relation_name | 更新关系名称 | admin:group_control |
| GET | /api/group/page/by_group | 按组织查用户 | user:read |
| GET | /api/group/page/by_user | 查用户所在组织 | user:read |

### IT 工单管理 (zx_topbiz)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | /ticket/create | 创建工单 | JSON Body: ItTicket |
| PUT | /ticket/update | 更新工单 | JSON Body: ItTicket |
| POST | /ticket/assign | 分配工单 | ticketId, handlerId, handlerName |
| POST | /ticket/handle | 处理工单 | ticketId, solution, status |
| POST | /ticket/close | 关闭工单 | ticketId, remark |
| GET | /ticket/page | 分页查询 | pageNum, pageSize, status, ticketType, priority, keyword |
| GET | /ticket/{id} | 工单详情 | 路径参数 |
| DELETE | /ticket/{id} | 删除工单 | 路径参数(逻辑删除) |

### IT 资产管理 (zx_topbiz)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | /asset/create | 创建资产 | JSON Body: ItAsset |
| PUT | /asset/update | 更新资产 | JSON Body: ItAsset |
| POST | /asset/assign | 分配资产 | assetId, userId, userName |
| POST | /asset/recycle | 回收资产 | assetId |
| POST | /asset/scrap | 报废资产 | assetId, remark |
| GET | /asset/page | 分页查询 | pageNum, pageSize, status, assetType, keyword |
| GET | /asset/{id} | 资产详情 | 路径参数 |
| GET | /asset/code/{assetCode} | 按编号查询 | 路径参数 |
| DELETE | /asset/{id} | 删除资产 | 路径参数(逻辑删除) |

### 审批流程 (zx_topbiz)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | /approval/create | 创建审批 | JSON Body: ItApproval |
| POST | /approval/approve | 审批通过 | approvalId, opinion |
| POST | /approval/reject | 审批拒绝 | approvalId, opinion |
| POST | /approval/cancel | 取消申请 | approvalId |
| GET | /approval/page | 分页查询 | pageNum, pageSize, status, approvalType |
| GET | /approval/{id} | 审批详情 | 路径参数 |
| GET | /approval/ticket/{ticketId} | 按工单查审批 | 路径参数 |

### 开发记录 (zx_topbiz)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /development-record/add | 添加记录 | user:write 或 admin |
| PUT | /development-record/update | 更新记录 | user:update 或 admin |
| DELETE | /development-record/delete | 删除记录 | user:delete 或 admin |
| GET | /development-record/list-admin | 管理员查询 | admin:development_record_control |
| GET | /development-record/list-emp | 员工查询 | user:read 或 admin |

### 消息服务 (zx_msgservice)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | /msg/send/instant | 即时发送邮件 | JSON Body: SendDTO |
| POST | /msg/send/timing | 定时发送邮件 | JSON Body: SendDTO |
| POST | /msg/send/period-send | 周期发送邮件 | JSON Body: SendDTO |
| POST | /msg/template/getfixedtemplate | 查询指定模板 | templateId |
| POST | /msg/template/getalltemplates | 查询全部模板 | - |
| POST | /msg/template/createtemplate | 创建模板 | content, templateName |
| POST | /msg/template/updateTemplate | 更新模板 | id, newTemplateContent |
| POST | /msg/template/delete | 删除模板 | templateId |

### 日志服务 (zx_logservice)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | /log/httplog/page-query | 精确查询日志 | page, pageSize, clientIp, serviceName, logLevel, requestUri |
| GET | /log/httplog/page-search | 模糊搜索日志 | page, pageSize, requestUri 等 |
| GET | /log/http-statistics/num-visit | 时段访问统计 | startTime, endTime |
| GET | /log/http-statistics/high-frequency-ip | 高频 IP 检测 | threshold, startTime, endTime, serviceName |
| GET | /log/http-statistics/visit-chain | 访问链追踪 | requestId |

---

## 数据库设计

### MySQL 数据库

| 数据库 | 服务 | 核心表 |
|--------|------|--------|
| zx_user_service_db | zx_userservice | user, permission, group, permission_and_user, group_and_user, permission_and_group, group_and_group |
| zx_top_service_db | zx_topbiz | development_record, it_ticket, it_asset, it_approval |
| zx_msg_service_db | zx_msgservice | template, message |
| nacos_config | Nacos | Nacos 内置表 |

### ClickHouse 数据库

| 数据库 | 核心表 | 说明 |
|--------|--------|------|
| http_log_db | http_log | HTTP 请求日志，按月分区，90 天 TTL |
| http_log_db | visit_statistics | 按小时聚合的访问统计 |
| http_log_db | high_frequency_ip | 高频 IP 统计 |

### ER 关系概要

```
user ──1:N── permission_and_user ──N:1── permission
user ──1:N── group_and_user ──N:1── group
group ──1:N── group_and_group ──N:1── group (父子)

it_ticket ──1:N── it_approval
user ──1:N── it_ticket (申请人/处理人)
user ──1:N── it_asset (使用人)
user ──1:N── it_approval (申请人/审批人)
```

---

## 配置参考

### 环境变量

| 变量 | 说明 | 默认值 |
|------|------|--------|
| `MYSQL_ROOT_PASSWORD` | MySQL root 密码 | root123456 |
| `MYSQL_HOST` | MySQL 地址 | localhost |
| `MYSQL_PORT` | MySQL 端口 | 3306 |
| `REDIS_HOST` | Redis 地址 | localhost |
| `REDIS_PORT` | Redis 端口 | 6379 |
| `REDIS_PASSWORD` | Redis 密码 | (空) |
| `NACOS_SERVER` | Nacos 地址 | localhost:8848 |
| `NACOS_NAMESPACE` | Nacos 命名空间 | (空) |
| `CLICKHOUSE_HOST` | ClickHouse 地址 | localhost |
| `CLICKHOUSE_PORT` | ClickHouse 端口 | 8123 |
| `JWT_SECRET` | JWT 签名密钥 | JavaFormITOpsPlatformSecretKey2024 |
| `JWT_EXPIRATION` | JWT 过期时间(ms) | 86400000 (24h) |
| `MAIL_HOST` | 邮件服务器 | smtp.163.com |
| `MAIL_USERNAME` | 邮件账号 | (需配置) |
| `MAIL_PASSWORD` | 邮件授权码 | (需配置) |

### 端口分配

| 服务 | 端口 | 说明 |
|------|------|------|
| zx_gateway | 9000 | API 网关，对外唯一入口 |
| zx_userservice | 8081 | 用户服务 |
| zx_topbiz | 8082 | 业务聚合服务 |
| zx_msgservice | 8083 | 消息服务 |
| zx_logservice | 8084 | 日志服务 |
| MySQL | 3306 | 关系数据库 |
| Redis | 6379 | 缓存 |
| Nacos | 8848 / 9848 | 注册中心 |
| ClickHouse | 8123 / 9000 | 日志数据库 |

### 默认账号

| 系统 | 账号 | 密码 |
|------|------|------|
| 平台管理员 | admin | admin123 |
| Nacos 控制台 | nacos | nacos |
| MySQL | root | root123456 (可在 .env 修改) |

---

## 常见问题

### 1. docker-compose up 后服务没有注册到 Nacos

Nacos 初始化需要 MySQL 先就绪。docker-compose 已配置了 `depends_on + healthcheck`，如果仍失败，手动等 MySQL 启动后重启 Nacos：

```bash
docker-compose restart nacos
```

### 2. 编译报 "Cannot resolve symbol" 或依赖找不到

```bash
# 先安装公共模块到本地仓库
mvn install -pl zx_common -am -DskipTests
```

### 3. 邮件发送失败

需要在 `.env` 中配置真实的 `MAIL_USERNAME` 和 `MAIL_PASSWORD`（163 邮箱使用授权码而非登录密码）。

### 4. ClickHouse 连接报错

确认 ClickHouse 已正常启动，且 `http_log_db` 数据库和表已创建：

```bash
docker-compose exec clickhouse clickhouse-client --query "SHOW DATABASES"
```

### 5. 请求返回 401 Unauthorized

- 确认请求头携带 `Authorization: Bearer {token}`
- 确认 Token 未过期（默认 24 小时）
- 登录/注册/验证码接口不需要 Token

### 6. JDK 版本不兼容

本项目使用 **JDK 17**。zx_userservice 原 pom 中写了 java.version=21，已统一为 17。如果本地有多版本 JDK，请确认 `JAVA_HOME` 指向 17。

### 7. 如何停止所有服务

```bash
docker-compose down          # 停止并移除容器
docker-compose down -v       # 停止并清除数据卷（慎用，会删数据）
```

---

## 相关文档

- [架构设计文档](docs/ARCHITECTURE.md) -- 详细的系统架构、模块设计、状态机、安全设计、部署架构
- [API 接口文档](docs/API.md) -- 每个接口的请求/响应示例、字段说明、cURL 调用示例
- [数据库初始化脚本](docker/mysql/init/01_init.sql) -- 完整建表语句和初始数据

---

## License

MIT License
