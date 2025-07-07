package com.homework.zx_logservice.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * 链首过滤器：把原始 request / response 包装成可重复读取的缓存包装类。
 */
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // —— 包装 request —— //
        ContentCachingRequestWrapper reqWrapper =
                (request instanceof ContentCachingRequestWrapper r)
                        ? r
                        : new ContentCachingRequestWrapper(request);

        // —— 包装 response —— //
        ContentCachingResponseWrapper respWrapper =
                (response instanceof ContentCachingResponseWrapper r)
                        ? r
                        : new ContentCachingResponseWrapper(response);

        try {
            // 链式调用，后续拦截器/控制器都看到包装后的对象
            filterChain.doFilter(reqWrapper, respWrapper);
        } finally {
            // 最终写回响应体，避免客户端拿不到数据
            respWrapper.copyBodyToResponse();
        }
    }
}