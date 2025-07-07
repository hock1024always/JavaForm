# 基础镜像
FROM openjdk:17.0
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 拷贝jar包
COPY ./target/Zx_Msgservice-0.0.1-SNAPSHOT.jar /app.jar
# 指定项目监听的端口
EXPOSE 8081
# 入口
ENTRYPOINT ["java", "-jar", "/app.jar"]