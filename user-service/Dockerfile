FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle :user-service:build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/user-service/build/libs/*.jar app.jar

COPY --from=builder /app/user-service/src/main/resources/application.yml /app/application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]
