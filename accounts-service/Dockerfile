FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar accounts-service.jar
ENTRYPOINT ["java","-jar","/accounts-service.jar"]