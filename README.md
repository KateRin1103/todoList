Generate .jar file command:
mvn clean install -DskipTests=true

Create docker container command:
docker compose up

Show all containers command in terminal:
docker ps

Check created database command in terminal:
docker exec -it {postgres CONTAINER_ID} psql -U postgres todo_db
\dt