FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle :eureka-server:build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/eureka-server/build/libs/*.jar app.jar

COPY --from=builder /app/eureka-server/src/main/resources/application.yml /app/application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]
