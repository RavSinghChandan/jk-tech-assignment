# Build stage
FROM maven:3.8.4-openjdk-17-slim as build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/document-management-*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"] 