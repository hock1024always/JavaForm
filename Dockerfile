# JavaForm IT运维管理平台 - 构建阶段
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# 复制Maven配置
COPY pom.xml .
COPY zx_common/pom.xml zx_common/
COPY zx_gateway/pom.xml zx_gateway/
COPY zx_userservice/pom.xml zx_userservice/
COPY zx_topbiz/pom.xml zx_topbiz/
COPY zx_msgservice/pom.xml zx_msgservice/
COPY zx_logservice/pom.xml zx_logservice/

# 复制Maven Wrapper
COPY mvnw .
COPY .mvn .mvn

# 下载依赖
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY zx_common/src zx_common/src
COPY zx_gateway/src zx_gateway/src
COPY zx_userservice/src zx_userservice/src
COPY zx_topbiz/src zx_topbiz/src
COPY zx_msgservice/src zx_msgservice/src
COPY zx_logservice/src zx_logservice/src

# 构建项目
RUN ./mvnw clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 创建非root用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# 设置时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

USER appuser

# 构建参数，用于指定要构建的服务
ARG SERVICE_NAME

# 复制构建产物
COPY --from=builder /app/${SERVICE_NAME}/target/*.jar app.jar

# JVM参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
