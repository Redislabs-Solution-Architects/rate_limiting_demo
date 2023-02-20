FROM openjdk:8-jdk-alpine
RUN addgroup -S abhishek && adduser -S abhishek -G abhishek
USER abhishek:abhishek
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]