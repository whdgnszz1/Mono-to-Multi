FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle :order-service:build -x test --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/order-service/build/libs/*.jar app.jar

COPY --from=builder /app/order-service/src/main/resources/application.yml /app/application.yml

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]
