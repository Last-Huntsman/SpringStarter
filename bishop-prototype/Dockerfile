# Этап сборки
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY ../ .
RUN mvn -pl bishop-prototype -am clean package -DskipTests

# Этап запуска
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /build/bishop-prototype/target/bishop-prototype-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
