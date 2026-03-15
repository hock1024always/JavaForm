package com.zxzy.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 * @author JavaForm
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    FAIL(400, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    TOKEN_EXPIRED(401, "Token已过期，请重新登录"),
    TOKEN_INVALID(401, "Token无效"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),

    // 业务错误 5xx
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    // 用户相关 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_DISABLED(1004, "账号已被禁用"),
    VERIFICATION_CODE_ERROR(1005, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(1006, "验证码已过期"),
    EMAIL_EXISTS(1007, "邮箱已被注册"),

    // 权限相关 2xxx
    PERMISSION_DENIED(2001, "权限不足"),
    ROLE_NOT_FOUND(2002, "角色不存在"),

    // 工单相关 3xxx
    TICKET_NOT_FOUND(3001, "工单不存在"),
    TICKET_STATUS_ERROR(3002, "工单状态错误"),
    TICKET_ALREADY_PROCESSED(3003, "工单已处理"),

    // 资产相关 4xxx
    ASSET_NOT_FOUND(4001, "资产不存在"),
    ASSET_IN_USE(4002, "资产使用中，无法删除"),

    // 消息相关 5xxx
    MESSAGE_SEND_FAIL(5001, "消息发送失败"),
    TEMPLATE_NOT_FOUND(5002, "模板不存在");

    private final Integer code;
    private final String message;
}
