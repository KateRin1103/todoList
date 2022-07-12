#FROM openjdk:17-alpine
#ARG JAR_FILE=target/todo-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} todo.jar
#ENTRYPOINT ["java","-jar","todo.jar"]

FROM maven:3.8.3-openjdk-17
WORKDIR /app
COPY . /app
RUN mvn package
EXPOSE 8085
CMD ["mvn", "spring-boot:run"]