# API del microservicio ai-assistant-ms

Guia rapida para probar el modulo de IA de InkluSport.  
Puerto por defecto: **8087**  
Base URL local: `http://localhost:8087`

---

## Antes de probar

1. Ten Mongo corriendo (por defecto apunta a `mongodb://localhost:27017/inklusport_ia`).
2. Levanta el microservicio:

```bash
cd ai-assistant-ms
mvn spring-boot:run
```

Si algo falla, revisa que Mongo este prendido y que el puerto 8087 no este ocupado.

---

## Que hace cada cosa (sin vueltas)

| Ruta | Para que sirve |
|------|----------------|
| `POST /api/ia/analisis` | Guarda un analisis biomecanico. Calcula puntaje y recomendaciones solo. |
| `GET /api/ia/analisis/usuario/{usuarioId}` | Lista el historial de un usuario. |
| `PUT /api/ia/planes` | Crea o actualiza un plan de entrenamiento con adaptaciones. |
| `POST /api/ia/chat` | Manda un mensaje al chatbot y guarda la conversacion. |

### Sobre la "IA" de verdad

- **Analisis biomecanico:** no usa ChatGPT ni nada parecido. Usa una formula con los 3 valores que mandas (rango, simetria, estabilidad).
- **Plan:** guarda lo que tu mandas en el JSON. No inventa ejercicios todavia.
- **Chatbot:** por ahora lee palabras como "hola", "ayuda", "progreso" y marca la intencion. Despues se puede enchufar un LLM por detras sin cambiar la URL.

---

## 1. Registrar analisis biomecanico

**POST** `/api/ia/analisis`

Body (JSON):

```json
{
  "usuarioId": "user-001",
  "tipoDiscapacidad": "MOTORA",
  "rangoMovimiento": 65,
  "simetria": 70,
  "estabilidad": 60
}
```

Los numeros van de **0 a 100**.

Respuesta esperada: `201 Created` con el puntaje y una lista de recomendaciones.

Formula que usa el backend:

```
puntaje = (rangoMovimiento * 0.4) + (simetria * 0.3) + (estabilidad * 0.3)
```

---

## 2. Ver historial de un usuario

**GET** `/api/ia/analisis/usuario/user-001`

Respuesta: lista de analisis del mas nuevo al mas viejo.

Si el usuario no tiene nada guardado, responde **404**.

---

## 3. Crear o actualizar plan de entrenamiento

**PUT** `/api/ia/planes`

Body (JSON):

```json
{
  "usuarioId": "user-001",
  "entrenadorId": "coach-99",
  "ejercicios": [
    {
      "nombreEjercicio": "Sentadilla asistida",
      "adaptaciones": ["voz", "visual", "cognitiva"]
    },
    {
      "nombreEjercicio": "Estiramiento de hombros",
      "adaptaciones": ["visual"]
    }
  ]
}
```

Si ya habia un plan con el mismo `usuarioId` y `entrenadorId`, se **actualiza**. Si no, se crea uno nuevo.

---

## 4. Enviar mensaje al chatbot

**POST** `/api/ia/chat`

Body (JSON):

```json
{
  "usuarioId": "user-001",
  "mensaje": "hola necesito ayuda con mi progreso"
}
```

El servicio intenta detectar intencion:

| Si el mensaje tiene algo como... | Intencion |
|----------------------------------|-----------|
| hola, buenas | SALUDO |
| ayuda, soporte | AYUDA |
| progreso, avance | PROGRESO |
| gracias, adios | CIERRE (y cierra la conversacion) |
| cualquier otra cosa | CONSULTA_GENERAL |

La conversacion se guarda en Mongo en la coleccion `conversaciones_chatbot`.

---

## Errores comunes

| Codigo | Significado |
|--------|-------------|
| 400 | Datos malos o faltan campos (validacion). |
| 404 | No hay historial para ese usuario (solo en GET analisis). |

Ejemplo de error de validacion:

```json
{
  "timestamp": "2026-05-28T10:00:00",
  "status": 400,
  "error": "Validation Error",
  "message": "El rangoMovimiento es obligatorio",
  "path": "/api/ia/analisis"
}
```

---

## Colecciones en Mongo

| Coleccion | Que guarda |
|-----------|------------|
| `analisis_biomecanicos` | Analisis con puntaje y recomendaciones |
| `planes_entrenamiento` | Planes con ejercicios y adaptaciones |
| `conversaciones_chatbot` | Mensajes e intencion del chat |

---

## Proximo paso (cuando quieran IA de verdad)

1. Crear un `LlmClient` aparte (OpenAI u Ollama en local).
2. Llamarlo desde `ChatbotServiceImpl` y guardar la respuesta del bot.
3. Opcional: endpoint para sugerir planes con prompt + JSON.

Eso va en otro PR para no mezclar todo.

---

Hecho para el equipo de InkluSport. Si algo no cuadra, revisa los controllers en `com.inklusport.ia.controller`.
