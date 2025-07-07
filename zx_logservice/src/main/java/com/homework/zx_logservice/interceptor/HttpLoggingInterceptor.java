package com.homework.zx_logservice.interceptor;

import cn.hutool.core.util.StrUtil;
import com.homework.zx_logservice.domain.po.HttpLog;
import com.homework.zx_logservice.logger.JsonLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.zalando.logbook.Logbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String RESPONSE_WRAPPER = "response_wrapper";
    @Value("${spring.application.name:default-service-name}")
    private  String serviceName;
    private final String loggerName = "http-logger";
    private final Logbook logbook; // 注入 Logbook

    private Map<String, HttpLog> map = new ConcurrentHashMap<>();

    public HttpLoggingInterceptor(Logbook logbook) {
        this.logbook = logbook;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long now = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        if(request.getAttribute(REQUEST_ID_ATTRIBUTE) == null || StrUtil.isBlank(request.getAttribute(REQUEST_ID_ATTRIBUTE).toString())){
            request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
        } else {
            requestId = request.getAttribute(REQUEST_ID_ATTRIBUTE).toString();
        }
        request.setAttribute(START_TIME_ATTRIBUTE, now);
        MDC.put("requestId", requestId); // 可选：放入 MDC 以便其他日志关联
        HttpLog log = new HttpLog();
        log.setTimestamp(now);
        log.setLogLevel("INFO"); // 默认级别
        log.setLogger(loggerName); // 获取处理器类名
        log.setServiceName(serviceName);
        log.setRequestId(requestId);
        log.setRequestTime(now);
        log.setClientIp(request.getRemoteAddr());
        log.setRequestMethod(request.getMethod());
        log.setRequestUri(request.getRequestURI());
        log.setRequestProtocol(request.getProtocol());
        log.setRequestHeaders(getHeaders(request));
        String requestBodyString = getBody(request);
        log.setRequestBody(requestBodyString);
        long contentLengthLong = request.getContentLengthLong();
        if (contentLengthLong < 0) {
            contentLengthLong=0;
        }
        log.setRequestSize(contentLengthLong);

        //放入map
        map.put(requestId, log);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long endTime = System.currentTimeMillis();
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);

        HttpLog log = map.get(requestId);
        log.setTimestamp(endTime);
        log.setLogLevel(response.getStatus() == 200 ? "INFO" : "ERROR");

        log.setResponseTime(endTime);
        log.setResponseStatusCode(response.getStatus());
        log.setResponseLocale(response.getLocale().toString());
        log.setResponseHeaders(getHeaders(response));
        request.getAttribute(REQUEST_ID_ATTRIBUTE);
        String responseBodyString = getBody(response);
        log.setResponseBody(responseBodyString);
        log.setResponseSize(responseBodyString.length());
        log.setResponseDuration(startTime != null ? endTime - startTime : 0);

        Map<String, Object> optionalInfo = new HashMap<>();
        optionalInfo.put("thread_id", Thread.currentThread().getName());
        if (ex != null) {
            optionalInfo.put("exception_info", ex.toString());
        }
        log.setOptional(optionalInfo);

        //计入
        JsonLogger.logEntity(log);

        map.remove(requestId);
        MDC.remove("requestId");
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(name -> headers.put(name, request.getHeader(name)));
        return headers;
    }
    /* ============ 请求体 ============ */


//    private String getBody(ContentCachingRequestWrapper request) {
//        byte[] body = request.getContentAsByteArray();
//        if (body.length == 0) return "";
//        String charset = request.getCharacterEncoding() != null
//                ? request.getCharacterEncoding()
//                : StandardCharsets.UTF_8.name();
//        try {
//            return new String(body, charset);
//        } catch (UnsupportedEncodingException e) {
//            log.error("Failed to decode request body with charset {}", charset, e);
//            return new String(body, StandardCharsets.UTF_8); // 兜底
//        }
//    }

    private String getBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] body = wrapper.getContentAsByteArray();
            if (body.length == 0) return "";
            String charset = wrapper.getCharacterEncoding() != null
                    ? wrapper.getCharacterEncoding()
                    : StandardCharsets.UTF_8.name();
            try {
                return new String(body, charset);
            } catch (UnsupportedEncodingException e) {
                return new String(body, StandardCharsets.UTF_8);
            }
        }
        return "";
    }

    /* ============ 响应体 ============ */
    private String getBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper wrapper) {
            byte[] body = wrapper.getContentAsByteArray();
            if (body.length == 0) return "";

            String charset = wrapper.getCharacterEncoding() != null
                    ? wrapper.getCharacterEncoding()
                    : StandardCharsets.UTF_8.name();
            try {
                return new String(body, charset);
            } catch (UnsupportedEncodingException e) {
                return new String(body, StandardCharsets.UTF_8);
            } finally {
                /* 必须写回客户端，否则浏览器收不到响应体 ★ */
                try { wrapper.copyBodyToResponse(); } catch (IOException ignored) {}
            }
        }
        return "";
    }



    private Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        response.getHeaderNames().forEach(name -> headers.put(name, response.getHeader(name)));
        return headers;
    }



}