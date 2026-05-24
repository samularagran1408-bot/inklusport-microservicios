# Inklusport — Microservicios

Monorepositorio de microservicios Spring Boot para Inklusport.

## Servicios

| Carpeta | Descripción |
|---------|-------------|
| `eureka-server` | Registro de servicios |
| `api-gateway` | API Gateway |
| `ink-ms-common` | Librería compartida (DTOs; sin YAML ni JPA) |
| `ink-ms-auth` | Autenticación |
| `ink-ms-users` | Usuarios |
| `sports-events-ms` | Eventos deportivos |
| `accessibility-ms` | Accesibilidad |
| `admin-ms` | Administración |
| `analytics-ms` | Analítica |
| `search-ms` | Búsqueda |
| `ai-assistant-ms` | Asistente IA |

## Requisitos

- Java 17+ (o la versión que definas en cada `pom.xml`)
- Maven 3.9+
- Docker (opcional, para `docker-compose`)

## Arranque local

Desde la **raíz** del monorepo (instala `ink-ms-common` y arranca un servicio):

```bash
# Compilar todo el módulo activo
mvn clean install -DskipTests

# Auth (puerto 3001)
mvn spring-boot:run -pl ink-ms-auth

# Users (puerto 3002)
mvn spring-boot:run -pl ink-ms-users
```

Solo la librería compartida:

```bash
mvn clean install -pl ink-ms-common -am -DskipTests
```

Ver `ink-ms-common/README.md` para probar el JAR sin levantar un microservicio.

Con Docker Compose (cuando configures los servicios en `docker-compose.yml`):

```bash
docker compose up --build
```
