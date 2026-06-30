FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/ink-ms-ai-assistant.jar app.jar

EXPOSE 3008

ENTRYPOINT ["java", "-jar", "app.jar"]