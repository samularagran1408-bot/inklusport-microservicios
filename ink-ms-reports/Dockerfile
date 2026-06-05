# Build from repo root: docker compose build ink-ms-reports
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
RUN apk add --no-cache maven
COPY pom.xml .
COPY ink-ms-common/pom.xml ink-ms-common/pom.xml
COPY ink-ms-common/src ink-ms-common/src
COPY ink-ms-reports/pom.xml ink-ms-reports/pom.xml
COPY ink-ms-reports/src ink-ms-reports/src
RUN mvn -pl ink-ms-reports -am package -DskipTests -B -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/ink-ms-reports/target/ink-ms-reports-*.jar app.jar
EXPOSE 3006
ENTRYPOINT ["java", "-jar", "app.jar"]
