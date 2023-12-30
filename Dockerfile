FROM maven:3.9.2-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-jdk-alpine
COPY --from=build /home/app/target/*.jar /usr/local/lib/infinity-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/infinity-api.jar"]