FROM gradle:8.7-jdk17 AS backend-builder
WORKDIR /app
COPY . .
RUN gradle clean build -Pprofile=prod -x test

FROM openjdk:17-jdk-slim
COPY --from=backend-builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]