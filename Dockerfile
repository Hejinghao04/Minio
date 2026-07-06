# 后端 Dockerfile —— 多阶段构建
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY target/Minio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
