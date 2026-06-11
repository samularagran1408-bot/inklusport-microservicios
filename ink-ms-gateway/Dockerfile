# Build from repo root: docker compose build ink-ms-gateway
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml .
COPY ink-ms-gateway/pom.xml ink-ms-gateway/pom.xml
COPY ink-ms-gateway/src ink-ms-gateway/src
RUN mvn -pl ink-ms-gateway -am package -DskipTests -B -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ink-ms-gateway/target/ink-ms-gateway-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
