package com.homework.topbiz.config;


import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignConfig {
    /**
     * 这个方法是定义输出OpenFeign的日志
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
    private static final String REQUEST_ID_ATTRIBUTE = "requestId";
    @Bean
    public RequestInterceptor userRequestInterceptor(){

        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {

                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    String header = attributes.getRequest().getHeader(REQUEST_ID_ATTRIBUTE);
                    if (header != null) {
                        requestTemplate.header(REQUEST_ID_ATTRIBUTE, header);
                        log.info("Feign request with header {}.", header);
                    } else {
                        log.error("Feign request without header.");
                    }
                }
            }
        };
    }
}
