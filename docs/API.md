# JavaForm IT运维管理平台 - API接口文档

## 概述

- **Base URL**: `http://localhost:9000/api`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

### 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 一、用户认证接口

### 1.1 用户名密码登录

**请求**
```
POST /user/user/loginwithnameandpassword
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**响应示例**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": "1",
      "username": "admin",
      "email": "admin@javaform.com"
    }
  }
}
```

### 1.2 邮箱验证码登录

**请求**
```
POST /user/user/loginwithemailandverificationcode
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| email | String | 是 | 邮箱地址 |

**生成验证码**
```
POST /user/user/generateverificationcode?email=user@example.com
```

**验证验证码**
```
POST /user/user/checkverificationcode
参数: email, verificationCode
```

### 1.3 用户注册

**请求**
```
POST /user/user/userregister
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| email | String | 是 | 邮箱地址 |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

### 1.4 用户登出

**请求**
```
POST /user/user/userlogout
Authorization: Bearer {token}
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |

### 1.5 检查登录状态

**请求**
```
POST /user/user/checkstatus
Authorization: Bearer {token}
```

---

## 二、IT工单接口

### 2.1 创建工单

**请求**
```
POST /biz/ticket/create
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "title": "电脑无法开机",
  "description": "按下电源键后无反应，指示灯不亮",
  "ticketType": "HARDWARE",
  "priority": "HIGH",
  "institutionId": "org001",
  "expectedTime": "2024-01-15 18:00:00"
}
```

**字段说明**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 工单标题 |
| description | String | 否 | 工单描述 |
| ticketType | String | 是 | 类型: HARDWARE/SOFTWARE/NETWORK/ACCOUNT/OTHER |
| priority | String | 是 | 优先级: LOW/MEDIUM/HIGH/URGENT |
| institutionId | String | 否 | 机构ID |
| expectedTime | DateTime | 否 | 期望完成时间 |

### 2.2 分页查询工单

**请求**
```
GET /biz/ticket/page?pageNum=1&pageSize=10&status=PENDING
Authorization: Bearer {token}
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |
| status | String | 否 | 状态筛选 |
| ticketType | String | 否 | 类型筛选 |
| priority | String | 否 | 优先级筛选 |
| applicantId | String | 否 | 申请人ID |
| handlerId | String | 否 | 处理人ID |
| keyword | String | 否 | 关键词搜索 |

**响应示例**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 100,
    "pages": 10,
    "list": [
      {
        "id": "ticket001",
        "title": "电脑无法开机",
        "description": "按下电源键后无反应",
        "ticketType": "HARDWARE",
        "priority": "HIGH",
        "status": "PENDING",
        "applicantId": "user001",
        "applicantName": "张三",
        "createTime": "2024-01-10 09:00:00"
      }
    ]
  }
}
```

### 2.3 分配工单

**请求**
```
POST /biz/ticket/assign
Authorization: Bearer {token}
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ticketId | String | 是 | 工单ID |
| handlerId | String | 是 | 处理人ID |
| handlerName | String | 是 | 处理人姓名 |

### 2.4 处理工单

**请求**
```
POST /biz/ticket/handle
Authorization: Bearer {token}
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ticketId | String | 是 | 工单ID |
| solution | String | 是 | 解决方案 |
| status | String | 是 | 状态: RESOLVED/PROCESSING |

### 2.5 关闭工单

**请求**
```
POST /biz/ticket/close?ticketId=ticket001&remark=问题已解决
Authorization: Bearer {token}
```

### 2.6 查询工单详情

**请求**
```
GET /biz/ticket/{id}
Authorization: Bearer {token}
```

---

## 三、IT资产接口

### 3.1 创建资产

**请求**
```
POST /biz/asset/create
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "assetCode": "IT-2024-001",
  "assetName": "Dell Latitude 5540",
  "assetType": "COMPUTER",
  "brand": "Dell",
  "model": "Latitude 5540",
  "serialNumber": "SN123456789",
  "institutionId": "org001",
  "location": "3楼研发部",
  "purchaseDate": "2024-01-01",
  "purchaseAmount": 8000.00,
  "warrantyExpiry": "2027-01-01",
  "configuration": "{\"cpu\":\"i7-1365U\",\"ram\":\"16GB\",\"disk\":\"512GB SSD\"}"
}
```

**字段说明**
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| assetCode | String | 是 | 资产编号(唯一) |
| assetName | String | 是 | 资产名称 |
| assetType | String | 是 | 类型: COMPUTER/MONITOR/PRINTER/NETWORK/SERVER/OTHER |
| brand | String | 否 | 品牌 |
| model | String | 否 | 型号 |
| serialNumber | String | 否 | 序列号 |
| location | String | 否 | 存放位置 |
| purchaseDate | Date | 否 | 购买日期 |
| purchaseAmount | Decimal | 否 | 购买金额 |
| warrantyExpiry | Date | 否 | 保修截止日期 |
| configuration | String | 否 | 配置信息(JSON格式) |

### 3.2 分页查询资产

**请求**
```
GET /biz/asset/page?pageNum=1&pageSize=10&status=AVAILABLE&assetType=COMPUTER
Authorization: Bearer {token}
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码 |
| pageSize | Integer | 否 | 每页大小 |
| status | String | 否 | 状态: AVAILABLE/IN_USE/REPAIRING/SCRAPPED |
| assetType | String | 否 | 资产类型 |
| institutionId | String | 否 | 机构ID |
| keyword | String | 否 | 关键词(名称/编号/品牌) |

### 3.3 分配资产

**请求**
```
POST /biz/asset/assign
Authorization: Bearer {token}
Content-Type: application/x-www-form-urlencoded
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| assetId | String | 是 | 资产ID |
| userId | String | 是 | 用户ID |
| userName | String | 是 | 用户姓名 |

### 3.4 回收资产

**请求**
```
POST /biz/asset/recycle?assetId=asset001
Authorization: Bearer {token}
```

### 3.5 资产报废

**请求**
```
POST /biz/asset/scrap?assetId=asset001&remark=设备老化，无法维修
Authorization: Bearer {token}
```

---

## 四、审批流程接口

### 4.1 创建审批申请

**请求**
```
POST /biz/approval/create
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "ticketId": "ticket001",
  "approvalType": "TICKET_HANDLE",
  "title": "工单处理审批",
  "content": "需要购买新配件，预算2000元",
  "approverId": "manager001",
  "approverName": "李经理",
  "institutionId": "org001"
}
```

**审批类型**
| 类型 | 说明 |
|------|------|
| TICKET_HANDLE | 工单处理审批 |
| ASSET_APPLY | 资产申请审批 |
| ASSET_RETURN | 资产归还审批 |
| OTHER | 其他 |

### 4.2 审批通过

**请求**
```
POST /biz/approval/approve?approvalId=approval001&opinion=同意
Authorization: Bearer {token}
```

### 4.3 审批拒绝

**请求**
```
POST /biz/approval/reject?approvalId=approval001&opinion=预算超支，请重新评估
Authorization: Bearer {token}
```

### 4.4 取消申请

**请求**
```
POST /biz/approval/cancel?approvalId=approval001
Authorization: Bearer {token}
```

### 4.5 分页查询审批

**请求**
```
GET /biz/approval/page?pageNum=1&pageSize=10&status=PENDING
Authorization: Bearer {token}
```

---

## 五、消息服务接口

### 5.1 即时发送消息

**请求**
```
POST /msg/send/instant
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "carrierId": "email",
  "templateId": "VERIFICATION_CODE",
  "templateParams": {
    "code": "123456"
  },
  "receivers": ["user@example.com"],
  "subject": "验证码通知"
}
```

### 5.2 定时发送消息

**请求**
```
POST /msg/send/timing
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "carrierId": "email",
  "templateId": "TICKET_NOTIFY",
  "templateParams": {
    "username": "张三",
    "ticketTitle": "电脑维修",
    "status": "已解决"
  },
  "receivers": ["user@example.com"],
  "subject": "工单状态更新通知",
  "timing": "2024-01-15 10:00:00"
}
```

### 5.3 周期发送消息

**请求**
```
POST /msg/send/period-send
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**
```json
{
  "carrierId": "email",
  "templateId": "WEEKLY_REPORT",
  "receivers": ["manager@example.com"],
  "subject": "周报提醒",
  "cron": "0 0 9 ? * FRI"
}
```

---

## 六、日志服务接口

### 6.1 查询HTTP日志

**请求**
```
GET /log/httplog/page-query?page=1&pageSize=10&serviceName=zx-topbiz
Authorization: Bearer {token}
```

**参数**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | Long | 否 | 页码 |
| pageSize | Long | 否 | 每页大小 |
| clientIp | String | 否 | 客户端IP |
| serviceName | String | 否 | 服务名称 |
| logLevel | String | 否 | 日志级别 |
| requestUri | String | 否 | 请求路径 |

### 6.2 模糊搜索日志

**请求**
```
GET /log/httplog/page-search?page=1&pageSize=10&requestUri=/ticket
Authorization: Bearer {token}
```

### 6.3 访问统计

**请求**
```
GET /log/http-statistics/num-visit?startTime=2024-01-01T00:00:00&endTime=2024-01-31T23:59:59
Authorization: Bearer {token}
```

### 6.4 高频IP检测

**请求**
```
GET /log/http-statistics/high-frequency-ip?threshold=10&startTime=2024-01-01T00:00:00
Authorization: Bearer {token}
```

---

## 七、错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权，请先登录 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1001 | 用户不存在 |
| 1002 | 用户已存在 |
| 1003 | 密码错误 |
| 1004 | 账号已被禁用 |
| 1005 | 验证码错误 |
| 1006 | 验证码已过期 |
| 3001 | 工单不存在 |
| 3002 | 工单状态错误 |
| 4001 | 资产不存在 |
| 4002 | 资产使用中，无法删除 |
| 5001 | 消息发送失败 |

---

## 八、调用示例

### cURL示例

```bash
# 登录
curl -X POST "http://localhost:9000/api/user/user/loginwithnameandpassword" \
  -d "name=admin" \
  -d "password=admin123"

# 创建工单
curl -X POST "http://localhost:9000/api/biz/ticket/create" \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{"title":"电脑故障","ticketType":"HARDWARE","priority":"HIGH"}'

# 查询工单列表
curl -X GET "http://localhost:9000/api/biz/ticket/page?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer {token}"
```
