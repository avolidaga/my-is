## Build stage
FROM maven:3.8.4-openjdk-17 as build
COPY src src
COPY pom.xml .
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=build /target/tinder-0.0.1-SNAPSHOT.jar tinder-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","tinder-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080