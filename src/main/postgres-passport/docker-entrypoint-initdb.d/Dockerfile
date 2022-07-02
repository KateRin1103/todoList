# syntax=docker/dockerfile:1
FROM postgres:latest
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB postgres_db
ENV POSTGRES_USERNAME postgres_test_user
COPY init.sql /docker-entrypoint-initdb.d/