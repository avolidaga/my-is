FROM maven:3.8.4-openjdk-17 as build
COPY src src
COPY pom.xml .
RUN mvn clean install -DskipTests

FROM openjdk:17
COPY --from=build /target/info-service-2.6.1.jar /target/info-service-2.6.1.jar
ENTRYPOINT ["java","-jar","/target/info-service-2.6.1.jar"]