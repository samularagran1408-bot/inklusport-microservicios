FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn -q -B package -DskipTests && rm -rf /root/.m2

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ink-ms-auth-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
