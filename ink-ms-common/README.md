# ink-ms-common

Librería compartida del monorepo (JAR, **no** es una aplicación ejecutable).

## Contenido

- `com.inklusport.common.*` — DTOs transversales (p. ej. `ErrorResponse`)
- `com.inklusport.auth.*` — entidades, repositorios y DTOs de autenticación
- `com.inklusport.users.*` — entidades, repositorios y DTOs de usuarios

No incluye `application.yml`, `application.properties` ni clase `@SpringBootApplication`.

## Compilar solo la librería

Desde la raíz del repositorio:

```bash
mvn clean install -pl ink-ms-common -am -DskipTests
```

El JAR queda en `ink-ms-common/target/ink-ms-common-1.0.0.jar`.

## Verificar que el JAR contiene las clases

```bash
jar tf ink-ms-common/target/ink-ms-common-1.0.0.jar | findstr "User.class"
jar tf ink-ms-common/target/ink-ms-common-1.0.0.jar | findstr "ErrorResponse.class"
```

## Usar en un microservicio

En el `pom.xml` del MS:

```xml
<dependency>
    <groupId>com.inklusport</groupId>
    <artifactId>ink-ms-common</artifactId>
</dependency>
```

En la aplicación Spring Boot, activar escaneo JPA de la librería:

```java
@EntityScan("com.inklusport.users.entity")
@EnableJpaRepositories("com.inklusport.users.repository")
```

La configuración (BD, JWT, puertos) sigue viviendo **solo** en cada `ink-ms-*` con su `application.yml`.
