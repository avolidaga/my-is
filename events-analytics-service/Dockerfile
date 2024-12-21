FROM maven:3.8.4-openjdk-17 as build
COPY src src
COPY pom.xml .
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=build /target/events-analytics-service.jar /target/events-analytics-service.jar
ENTRYPOINT ["java","-jar","/target/events-analytics-service.jar"]