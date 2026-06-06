# Build from repo root: docker compose build ink-ms-users
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml .
COPY ink-ms-common/pom.xml ink-ms-common/pom.xml
COPY ink-ms-common/src ink-ms-common/src
COPY ink-ms-users/pom.xml ink-ms-users/pom.xml
COPY ink-ms-users/src ink-ms-users/src
RUN mvn -pl ink-ms-users -am package -DskipTests -B -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ink-ms-users/target/ink-ms-users-*.jar app.jar
EXPOSE 3002
ENTRYPOINT ["java", "-jar", "app.jar"]
