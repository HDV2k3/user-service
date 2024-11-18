# Stage 1: build
# Start with a Maven image that includes JDK 21
FROM maven:3.9.8-amazoncorretto-21 AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with Maven
RUN mvn package -DskipTests

# Stage 2: create image
# Start with Amazon Corretto JDK 21
FROM amazoncorretto:21.0.4

# Set working folder to /app and copy compiled file from the build stage
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Optionally, set environment variables if needed (for example, for database connections)
ENV DB_HOST=jdbc:postgresql://databaseidentity.cd8c028cinkb.ap-southeast-2.rds.amazonaws.com:5432/identity
ENV DB_PORT=5432

# Expose the port your app will run on
EXPOSE 8080

# Command to run the application, with custom JVM options or environment variables if needed
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]
