FROM maven:3.9.9-eclipse-temurin-24 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM openjdk:24-jdk
WORKDIR /app
ARG JAR_FILE=target/springboot25-0.0.1-SNAPSHOT.jar
COPY --from=builder /build/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
