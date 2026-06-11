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
| `ink-ms-reports` | Reportes y analítica |
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

# Reports (puerto 3003)
mvn spring-boot:run -pl ink-ms-reports
```

Solo la librería compartida:

```bash
mvn clean install -pl ink-ms-common -am -DskipTests
```

Ver `ink-ms-common/README.md` para probar el JAR sin levantar un microservicio.

Con Docker (ver guía completa en [`DOCKER.md`](DOCKER.md)):

```bash
# Stack núcleo: auth, users, sports, gateway
docker compose up --build

# Stack completo (+ reports, admin, accessibility, IA)
docker compose --profile full up --build
```

Gateway: http://localhost:8080
