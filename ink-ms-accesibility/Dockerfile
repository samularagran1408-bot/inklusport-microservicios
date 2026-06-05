# Build from repo root: docker compose build ink-ms-accessibility
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml .
COPY ink-ms-accesibility/pom.xml ink-ms-accesibility/pom.xml
COPY ink-ms-accesibility/src ink-ms-accesibility/src
RUN mvn -f ink-ms-accesibility/pom.xml package -DskipTests -B -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ink-ms-accesibility/target/ink-ms-accessibility-*.jar app.jar
EXPOSE 3004
ENTRYPOINT ["java", "-jar", "app.jar"]
