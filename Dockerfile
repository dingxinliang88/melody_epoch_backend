FROM maven:3.8.1-jdk-8-slim as builder

# 设置工作目录
WORKDIR /app

# 复制编译好的Spring Boot JAR文件到容器中
COPY target/your-spring-boot-app.jar /app/app.jar

# 复制外部配置文件到容器中
# TODO 修改为外部配置文件的路径
COPY /Users/codejuzi/Documents/CodeWorkSpace/Project/melody_epoch/config/conf.properties /app/conf.properties

# 设置环境变量，指定Spring Boot配置文件的位置
ENV SPRING_CONFIG_IMPORT=file:/app/conf.properties

# 暴露应用程序端口
EXPOSE 8080

# 启动Spring Boot应用
CMD ["java", "-jar", "/app/app.jar"]