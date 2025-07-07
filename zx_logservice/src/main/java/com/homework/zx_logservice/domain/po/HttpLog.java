package com.homework.zx_logservice.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("http_logs")
public class HttpLog {
    private Long timestamp; // 日志记录的时间戳
    private String logLevel; // 日志级别（如 INFO、ERROR 等）
    private String logger; // 记录日志的类名或组件名
    private String serviceName; // 所属服务的名称
    private String requestId; // 请求唯一标识符
    private Long requestTime; // 请求到达时间
    private String clientIp; // 发起请求的客户端 IP 地址
    private String requestMethod; // 请求使用的方法（如 GET、POST）
    private String requestUri; // 请求的 URI
    private String requestProtocol; // 请求使用的协议（如 HTTP/1.1）
    private Map<String, String> requestHeaders; // 请求头信息
    private String requestBody; // 请求体内容
    private Long requestSize; // 请求体大小（字节数）
    private Long responseTime; // 响应返回时间
    private int responseStatusCode; // 响应状态码（如 200、404）
    private String responseLocale; // 地域信息
    private Map<String, String> responseHeaders; // 响应头信息
    private String responseBody; // 响应体内容
    private long responseSize; // 响应体大小（字节数）
    private long responseDuration; // 请求-响应过程耗时（毫秒）
    private Map<String, Object> optional; // 额外可选字段，用于扩展信息
}
