FROM openjdk:17-jdk-slim
ENV SERVER_PORT=8080
ENV RDS_HOSTNAME=postgresql
ENV RDS_DB_NAME=pitch
ENV RDS_PORT=5432
ENV RDS_USERNAME=myuser
ENV RDS_PASSWORD=secret
ENV REDIS_PORT=6379
ENV REDIS_HOSTNAME=redis
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]