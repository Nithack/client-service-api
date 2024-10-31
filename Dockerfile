FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE ${CLIENT_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]