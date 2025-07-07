package com.homework.zx_logservice.logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // 用于 Java 8 时间类型
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class JsonLogger {

    // 获取在 logback.xml 中配置的特定 logger
    private static final Logger jsonEventLogger = LoggerFactory.getLogger("MyJsonLogger");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 注册 JavaTimeModule 以便正确序列化 LocalDateTime 等 Java 8 时间类型
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 你可以进一步配置 objectMapper，例如日期格式：
        // objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    public static void logEntity(Object entity) {
        if (entity == null) {
            return;
        }
        try {
            // 将实体对象转换为 JSON 字符串
            String jsonString = objectMapper.writeValueAsString(entity);
            // 使用特定的 logger 记录 JSON 字符串
            jsonEventLogger.info(jsonString);
        } catch (Exception e) {
            // 处理可能的序列化异常
            // 可以使用另一个通用 logger 来记录这个错误
            Logger errorLogger = LoggerFactory.getLogger(JsonLogger.class);
            errorLogger.error("Failed to serialize entity to JSON or log it: {}", entity.toString(), e);
        }
    }
}
