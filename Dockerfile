# Step 1: Build stage (Maven)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY ./pom.xml ./
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn clean package -DskipTests

# Step 2: Runtime stage (JDK only)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]
