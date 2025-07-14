# Step 1: Build using Maven and JDK 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven project files
COPY app/pom.xml .
COPY app/src ./src

# Package application (skip tests if you prefer)
RUN mvn clean package -DskipTests


# Step 2: Run with lightweight JRE 21
FROM eclipse-temurin:21.0.2_13-jre

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
