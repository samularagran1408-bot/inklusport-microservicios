# Inklusport — Microservicios

Monorepositorio de microservicios Spring Boot para Inklusport.

## Servicios

| Carpeta | Descripción |
|---------|-------------|
| `eureka-server` | Registro de servicios |
| `api-gateway` | API Gateway |
| `auth-ms` | Autenticación |
| `users-ms` | Usuarios |
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

Cada servicio se construye desde su carpeta:

```bash
cd <nombre-servicio>
mvn spring-boot:run
```

Con Docker Compose (cuando configures los servicios en `docker-compose.yml`):

```bash
docker compose up --build
```
