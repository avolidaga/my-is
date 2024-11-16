FROM maven:3.8.4-openjdk-17 as build
COPY src src
COPY pom.xml .
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=build /target/spacesuit-service-3.1.5.jar /target/spacesuit-service-3.1.5.jar
ENTRYPOINT ["java","-jar","/target/spacesuit-service-3.1.5.jar"]