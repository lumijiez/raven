FROM maven:3.9.9-eclipse-temurin-23 AS builder

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests -pl common,relay

FROM openjdk:23-jdk-slim

WORKDIR /app
COPY --from=builder /app/relay/target/relay-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
EXPOSE 8080