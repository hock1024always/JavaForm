package com.homework.topbiz.config; // 请确保包名正确

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // **1. 对所有 API 路径启用 CORS**
                // .allowedOrigins("*") // 2. **允许所有源**，仅用于开发环境！生产环境请指定具体域名
                .allowedOriginPatterns("*"
//                        "http://localhost:3000", // 示例：你的前端本地开发地址
//                        "https://your-production-frontend.com" // 示例：你的生产环境前端域名
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // **3. 允许的 HTTP 方法**
                .allowedHeaders("*") // **4. 允许所有请求头**，也可以列出具体头如 "Authorization", "Content-Type"
                .allowCredentials(true) // **5. 允许发送和接收凭证**（如 Cookies 或 Authorization 头）
                .maxAge(3600); // **6. 预检请求（OPTIONS）的缓存时间，单位秒** (1小时)
    }
}