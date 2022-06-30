# syntax=docker/dockerfile:1
FROM openjdk:17-alpine3.13

#WORKDIR /todoApp

#COPY src ./src

#CMD ["./mvnw", "spring-boot:run"]

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]