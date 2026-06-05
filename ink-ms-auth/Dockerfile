# Build from repo root: docker compose build ink-ms-auth
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml .
COPY ink-ms-common/pom.xml ink-ms-common/pom.xml
COPY ink-ms-common/src ink-ms-common/src
COPY ink-ms-auth/pom.xml ink-ms-auth/pom.xml
COPY ink-ms-auth/src ink-ms-auth/src
RUN mvn -pl ink-ms-auth -am package -DskipTests -B -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ink-ms-auth/target/ink-ms-auth-*.jar app.jar
EXPOSE 3001
ENTRYPOINT ["java", "-jar", "app.jar"]
