FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar camunda-orchestration.jar
ENTRYPOINT ["java","-jar","/camunda-orchestration.jar"]