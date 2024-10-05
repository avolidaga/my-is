## Build stage
#FROM maven:3.9.2-eclipse-temurin-17-alpine AS build
#
#WORKDIR /build
#
#COPY pom.xml .
#
#RUN mvn dependency:go-offline
#
#COPY src ./src
#
#RUN mvn clean package -DskipTests
#
## Runtime stage
#FROM openjdk:17-jdk-alpine
#
## Install PostgreSQL client tools in the runtime container
#RUN apk --no-cache add postgresql-client
#
#WORKDIR /app
#
## Copy the compiled application JAR from the build stage
#COPY --from=build /build/target/tinder-0.0.1-SNAPSHOT.jar /app/app.jar
#
#
#EXPOSE 8080
#
## Wait for the database to be ready, then run the application
#CMD java -jar /app/app.jar & \
#    echo "Waiting for PostgreSQL to be ready..." && \
#    until pg_isready -h "database" -p 5432 -U "$SPRING_DATASOURCE_USERNAME"; do \
#        sleep 1; \
#    done && \
#    echo "PostgreSQL is ready. Running SQL script." && \
#    wait
