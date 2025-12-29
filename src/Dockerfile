# =========================
# 1. Build Stage
# =========================
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

COPY src src

# 빌드
RUN ./gradlew clean bootJar --no-daemon


# =========================
# 2. Runtime Stage
# =========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 타임존
ENV TZ=Asia/Seoul

# 빌드 결과 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]