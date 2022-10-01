FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar credit-service.jar
ENTRYPOINT ["java","-jar","/credit-service.jar"]