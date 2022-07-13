FROM openjdk:17-alpine
ARG JAR_FILE=target/todo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} todo.jar
ENTRYPOINT ["java","-jar","todo.jar"]