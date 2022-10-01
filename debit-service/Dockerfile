FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar debit-service.jar
ENTRYPOINT ["java","-jar","/debit-service.jar"]