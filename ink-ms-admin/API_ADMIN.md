# API Admin - Microservicio de Administracion

Documentacion de los endpoints disponibles en el microservicio de administracion de Inklusport.

## Base URL

```
http://localhost:3005/api/v1/admin
```

## Autenticacion

Todos los endpoints (excepto health e info) requieren un token JWT en el encabezado:

```
Authorization: Bearer <token_jwt>
```

El usuario debe tener el rol `ADMIN` para acceder a los endpoints.

---

## Endpoints de Roles Administrativos

### Obtener todos los roles

```http
GET /roles
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "SUPER_ADMIN",
    "description": "Administrador con acceso total",
    "createdAt": "2024-01-15T10:30:00",
    "permissions": ["CREATE_USER", "DELETE_USER", "MANAGE_ROLES"],
    "adminCount": 2
  }
]
```

### Obtener un rol por ID

```http
GET /roles/{id}
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "SUPER_ADMIN",
  "description": "Administrador con acceso total",
  "createdAt": "2024-01-15T10:30:00",
  "permissions": ["CREATE_USER", "DELETE_USER", "MANAGE_ROLES"],
  "adminCount": 2
}
```

### Crear un rol

```http
POST /roles
```

**Body requerido:**
```json
{
  "name": "MODERATOR",
  "description": "Moderador de eventos"
}
```

**Respuesta exitosa (201 Created):**
```json
{
  "id": 3,
  "name": "MODERATOR",
  "description": "Moderador de eventos",
  "createdAt": "2024-01-15T10:30:00",
  "permissions": [],
  "adminCount": 0
}
```

### Actualizar un rol

```http
PUT /roles/{id}
```

**Body requerido:**
```json
{
  "name": "MODERATOR_UPDATED",
  "description": "Moderador actualizado"
}
```

**Respuesta exitosa (200 OK):** Rol actualizado

### Eliminar un rol

```http
DELETE /roles/{id}
```

**Respuesta exitosa (204 No Content)**

### Asignar un rol a un administrador

```http
POST /roles/assign
```

**Body requerido:**
```json
{
  "adminId": "uuid-del-admin",
  "roleId": 1,
  "assignedBy": "uuid-del-admin-asignador"
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "message": "Rol asignado exitosamente"
}
```

### Remover un rol de un administrador

```http
DELETE /roles/remove/{adminId}/{roleId}
```

**Respuesta exitosa (200 OK):**
```json
{
  "message": "Rol removido exitosamente"
}
```

---

## Endpoints de Auditoria

### Obtener acciones de un administrador

```http
GET /audit/admin/{adminId}?page=0&size=10
```

**Query Parameters:**
- `page`: Numero de pagina (default: 0)
- `size`: Cantidad de registros por pagina (default: 10)

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": "uuid",
      "adminId": "uuid-admin",
      "action": "USER_BLOCK",
      "targetType": "user",
      "targetId": "uuid-usuario",
      "details": "{...}",
      "ipAddress": "192.168.1.100",
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

### Obtener acciones por tipo

```http
GET /audit/by-action/{action}?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con acciones del tipo especificado

### Obtener acciones por rango de fechas

```http
GET /audit/by-date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59&page=0&size=10
```

**Query Parameters:**
- `startDate`: Fecha de inicio (formato ISO: yyyy-MM-ddTHH:mm:ss)
- `endDate`: Fecha de fin (formato ISO: yyyy-MM-ddTHH:mm:ss)
- `page`: Numero de pagina
- `size`: Cantidad de registros

**Respuesta exitosa (200 OK):** Pagina con acciones en el rango

### Obtener estadisticas de acciones

```http
GET /audit/statistics
```

**Respuesta exitosa (200 OK):**
```json
{
  "USER_BLOCK": 15,
  "EVENT_APPROVE": 8,
  "ROLE_ASSIGN": 12,
  "CONFIG_UPDATE": 5
}
```

---

## Endpoints de Aprobaciones

### Obtener aprobaciones pendientes

```http
GET /approvals?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con aprobaciones pendientes

### Obtener una aprobacion por ID

```http
GET /approvals/{id}
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": "uuid",
  "targetType": "event",
  "targetData": {...},
  "requestedBy": "uuid-admin",
  "requestedAt": "2024-01-15T10:30:00",
  "status": "pending",
  "reviewedBy": null,
  "reviewedAt": null,
  "reviewNotes": null
}
```

### Crear una aprobacion

```http
POST /approvals
```

**Body requerido:**
```json
{
  "targetType": "event",
  "targetData": {
    "name": "Evento nuevo",
    "description": "Descripcion del evento",
    "date": "2024-02-01"
  },
  "requestedBy": "uuid-admin"
}
```

**Respuesta exitosa (201 Created):** Aprobacion creada

### Aprobar una solicitud

```http
POST /approvals/{id}/approve
```

**Body requerido:**
```json
{
  "reviewedBy": "uuid-admin",
  "notes": "Aprobado. Los datos son correctos."
}
```

**Respuesta exitosa (200 OK):** Aprobacion actualizada con estado "approved"

### Rechazar una solicitud

```http
POST /approvals/{id}/reject
```

**Body requerido:**
```json
{
  "reviewedBy": "uuid-admin",
  "notes": "Rechazado. Falta informacion importante."
}
```

**Respuesta exitosa (200 OK):** Aprobacion actualizada con estado "rejected"

### Obtener aprobaciones por tipo

```http
GET /approvals/by-type/{targetType}?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con aprobaciones del tipo especificado

---

## Endpoints de Alertas

### Obtener todas las alertas

```http
GET /alerts?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con todas las alertas

### Obtener una alerta por ID

```http
GET /alerts/{id}
```

**Respuesta exitosa (200 OK):** Alerta encontrada

### Crear una alerta

```http
POST /alerts
```

**Body requerido:**
```json
{
  "type": "suspicious_activity",
  "severity": "high",
  "title": "Actividad sospechosa detectada",
  "description": "Se detecto multiples intentos fallidos de login",
  "targetId": "uuid-usuario",
  "targetType": "user"
}
```

**Respuesta exitosa (201 Created):** Alerta creada

### Resolver una alerta

```http
PUT /alerts/{id}/resolve?adminId=uuid-admin
```

**Respuesta exitosa (200 OK):** Alerta marcada como resuelta

### Obtener alertas sin resolver

```http
GET /alerts/unresolved?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con alertas activas

### Obtener alertas por gravedad

```http
GET /alerts/by-severity/{severity}?page=0&size=10
```

**Path Parameters:**
- `severity`: low, medium, high, critical

**Respuesta exitosa (200 OK):** Pagina con alertas de la gravedad especificada

---

## Endpoints de Configuracion de IA

### Obtener todas las configuraciones

```http
GET /ai-config
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "featureName": "biomechanical_analysis",
    "isEnabled": true,
    "modelVersion": "v2.1",
    "confidenceThreshold": 0.85,
    "parameters": {...},
    "updatedBy": "uuid-admin",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### Obtener configuracion por ID

```http
GET /ai-config/{id}
```

**Respuesta exitosa (200 OK):** Configuracion encontrada

### Obtener configuracion por nombre de funcionalidad

```http
GET /ai-config/by-feature/{featureName}
```

**Respuesta exitosa (200 OK):** Configuracion de la funcionalidad

### Crear configuracion

```http
POST /ai-config
```

**Body requerido:**
```json
{
  "featureName": "injury_prediction",
  "isEnabled": true,
  "modelVersion": "v1.5",
  "confidenceThreshold": 0.75,
  "parameters": {
    "threshold_alert": 0.8,
    "min_data_points": 10
  },
  "updatedBy": "uuid-admin"
}
```

**Respuesta exitosa (201 Created):** Configuracion creada

### Actualizar configuracion

```http
PUT /ai-config/{id}
```

**Body requerido:** Igual que crear

**Respuesta exitosa (200 OK):** Configuracion actualizada

### Habilitar funcionalidad

```http
PUT /ai-config/{id}/enable
```

**Respuesta exitosa (200 OK):** Funcionalidad habilitada

### Deshabilitar funcionalidad

```http
PUT /ai-config/{id}/disable
```

**Respuesta exitosa (200 OK):** Funcionalidad deshabilitada

---

## Endpoints de Reportes Programados

### Obtener todos los reportes

```http
GET /reports?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con reportes

### Obtener reporte por ID

```http
GET /reports/{id}
```

**Respuesta exitosa (200 OK):** Reporte encontrado

### Crear reporte

```http
POST /reports
```

**Body requerido:**
```json
{
  "name": "Reporte mensual de participantes",
  "type": "participants",
  "scheduleCron": "0 0 1 * *",
  "parameters": {
    "sport_id": "uuid",
    "include_disabled": true
  },
  "recipients": ["admin@example.com", "director@example.com"]
}
```

**Respuesta exitosa (201 Created):** Reporte creado

### Actualizar reporte

```http
PUT /reports/{id}
```

**Body requerido:** Igual que crear

**Respuesta exitosa (200 OK):** Reporte actualizado

### Eliminar reporte

```http
DELETE /reports/{id}
```

**Respuesta exitosa (204 No Content)**

### Obtener reportes por tipo

```http
GET /reports/by-type/{reportType}?page=0&size=10
```

**Path Parameters:**
- `reportType`: participants, attendance, disabilities, etc

**Respuesta exitosa (200 OK):** Pagina con reportes del tipo

---

## Endpoints de Configuracion del Sistema

### Obtener todas las configuraciones

```http
GET /config
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "configKey": "max_login_attempts",
    "configValue": "5",
    "description": "Intentos maximos de login antes de bloqueo",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

### Obtener configuracion por clave

```http
GET /config/{configKey}
```

**Respuesta exitosa (200 OK):** Configuracion encontrada

### Crear configuracion

```http
POST /config
```

**Body requerido:**
```json
{
  "configKey": "maintenance_mode",
  "configValue": "false",
  "description": "Modo de mantenimiento activado"
}
```

**Respuesta exitosa (201 Created):** Configuracion creada

### Actualizar configuracion

```http
PUT /config/{configKey}
```

**Body requerido:** Igual que crear

**Respuesta exitosa (200 OK):** Configuracion actualizada

### Eliminar configuracion

```http
DELETE /config/{configKey}
```

**Respuesta exitosa (204 No Content)**

---

## Endpoints de Permisos

### Obtener todos los permisos

```http
GET /permissions
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "CREATE_USER",
    "resource": "user",
    "action": "create",
    "description": "Crear nuevos usuarios"
  }
]
```

### Obtener permiso por ID

```http
GET /permissions/{id}
```

**Respuesta exitosa (200 OK):** Permiso encontrado

### Crear permiso

```http
POST /permissions
```

**Body requerido:**
```json
{
  "name": "DELETE_EVENT",
  "resource": "event",
  "action": "delete",
  "description": "Eliminar eventos de la plataforma"
}
```

**Respuesta exitosa (201 Created):** Permiso creado

### Actualizar permiso

```http
PUT /permissions/{id}
```

**Body requerido:** Igual que crear

**Respuesta exitosa (200 OK):** Permiso actualizado

### Obtener permisos de un rol

```http
GET /permissions/role/{roleId}
```

**Respuesta exitosa (200 OK):** Lista de permisos del rol

### Asignar permiso a rol

```http
POST /permissions/role/{roleId}/permission/{permissionId}
```

**Respuesta exitosa (200 OK):**
```json
{
  "message": "Permiso asignado exitosamente"
}
```

### Remover permiso de rol

```http
DELETE /permissions/role/{roleId}/permission/{permissionId}
```

**Respuesta exitosa (200 OK):**
```json
{
  "message": "Permiso removido exitosamente"
}
```

---

## Endpoints de Bloqueos de Usuarios

### Obtener usuarios bloqueados

```http
GET /user-blocks?page=0&size=10
```

**Respuesta exitosa (200 OK):** Pagina con usuarios bloqueados

### Obtener bloqueo por ID

```http
GET /user-blocks/{id}
```

**Respuesta exitosa (200 OK):** Bloqueo encontrado

### Obtener bloqueo de un usuario

```http
GET /user-blocks/user/{userId}
```

**Respuesta exitosa (200 OK):** Bloqueo del usuario

### Bloquear usuario

```http
POST /user-blocks
```

**Body requerido:**
```json
{
  "userId": "uuid-usuario",
  "blockType": "temporary",
  "reason": "Comportamiento inadecuado en eventos",
  "blockedBy": "uuid-admin"
}
```

**Respuesta exitosa (201 Created):** Bloqueo creado

### Desbloquear usuario

```http
DELETE /user-blocks/{id}
```

**Respuesta exitosa (200 OK):**
```json
{
  "message": "Usuario desbloqueado exitosamente"
}
```

### Obtener bloqueos por tipo

```http
GET /user-blocks/by-type/{blockType}?page=0&size=10
```

**Path Parameters:**
- `blockType`: temporary, permanent, etc

**Respuesta exitosa (200 OK):** Pagina con bloqueos del tipo

---

## Codigos de Error

| Codigo | Descripcion |
|--------|-------------|
| 200 | OK - Solicitud exitosa |
| 201 | Created - Recurso creado exitosamente |
| 204 | No Content - Operacion exitosa sin contenido de respuesta |
| 400 | Bad Request - Error en los datos enviados |
| 401 | Unauthorized - Token invalido o faltante |
| 403 | Forbidden - Usuario no autorizado |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Recurso duplicado |
| 500 | Internal Server Error - Error del servidor |

---

## Ejemplos de Uso

### Autenticarse y obtener token

Primero, obtener un token del microservicio de auth:

```bash
curl -X POST http://localhost:3001/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "email": "admin@example.com",
  "roles": ["ADMIN"]
}
```

### Usar el token para acceder a endpoints del admin

```bash
curl -X GET http://localhost:3005/api/v1/admin/roles \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

---

## Notas

- Todos los IDs son UUID excepto el ID de rol que es numerico (Integer)
- Las fechas se retornan en formato ISO 8601 (yyyy-MM-ddTHH:mm:ss)
- La paginacion usa parametros estandar: `page` (0-indexed) y `size`
- Los valores de enum deben enviarse en minusculas (ej: "pending", "approved")

