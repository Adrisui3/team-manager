# Build stage: compile the Spring Boot application
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /workspace/app

# Cache dependencies
COPY pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime stage: use a slim JRE image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the packaged Spring Boot jar from the builder image
COPY --from=builder /workspace/app/target/team-manager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]