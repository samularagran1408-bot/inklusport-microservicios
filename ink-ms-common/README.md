# ink-ms-common

Librería compartida del monorepo (JAR, **no** es una aplicación ejecutable).

## Contenido

- `com.inklusport.common.*` — DTOs transversales (p. ej. `ErrorResponse`)
- `com.inklusport.auth.dto.*` — DTOs de autenticación
- `com.inklusport.users.dto.*` — DTOs de usuarios

Las entidades y repositorios JPA viven en cada microservicio (`ink-ms-auth`, `ink-ms-users`), no en esta librería.

No incluye `application.yml`, `application.properties` ni clase `@SpringBootApplication`.

## Compilar solo la librería

Desde la raíz del repositorio:

```bash
mvn clean install -pl ink-ms-common -am -DskipTests
```

El JAR queda en `ink-ms-common/target/ink-ms-common-1.0.0.jar`.

## Verificar que el JAR contiene las clases

```bash
jar tf ink-ms-common/target/ink-ms-common-1.0.0.jar | findstr "ErrorResponse.class"
jar tf ink-ms-common/target/ink-ms-common-1.0.0.jar | findstr "LoginRequest.class"
```

## Usar en un microservicio

En el `pom.xml` del MS:

```xml
<dependency>
    <groupId>com.inklusport</groupId>
    <artifactId>ink-ms-common</artifactId>
</dependency>
```

La configuración (BD, JWT, puertos) y el modelo de persistencia (entidades, repositorios) viven **solo** en cada `ink-ms-*` con su `application.yml`.
