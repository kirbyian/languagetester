FROM maven:3.9.5 as build
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package -DskipTests -X

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Expose PostgreSQL port
EXPOSE 8080

# Copy the application JAR file into the container
COPY --from=build /app/target/*.jar app.jar

# Specify the command to run on container startup
CMD ["java", "-jar", "app.jar"]
