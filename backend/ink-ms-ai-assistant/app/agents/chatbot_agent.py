from app.services.grok_service import GrokService
from app.database.mongodb import get_db
from datetime import datetime

class ChatbotAgent:
    def __init__(self):
        self.grok = GrokService()

    async def procesar_mensaje(self, usuario_id: str, mensaje: str, discapacidad: str):
        db = get_db()

        # Buscar en base de conocimiento
        entrenamiento = await db.entrenamiento_chatbot.find_one({
            "activo": True,
            "palabras_clave": {"$in": mensaje.lower().split()}
        })

        if entrenamiento:
            respuesta = entrenamiento["respuesta_adaptada"].get(discapacidad, entrenamiento["respuesta_base"])
            intencion = entrenamiento["intencion"]
            adaptada = True
        else:
            # Usar Grok
            prompt = f"Usuario pregunta: {mensaje}\nDiscapacidad: {discapacidad}\nResponde de manera clara, profesional y adaptada."
            respuesta = await self.grok.chat(prompt, discapacidad)
            intencion = "general"
            adaptada = False

        # Guardar conversación
        await db.conversaciones_chatbot.update_one(
            {"usuario_id": usuario_id, "estado": "activa"},
            {
                "$push": {"mensajes": {"$each": [
                    {"mensaje": mensaje, "remitente": "usuario", "fecha": datetime.utcnow()},
                    {"mensaje": respuesta, "remitente": "asistente", "intencion": intencion, "fecha": datetime.utcnow()}
                ]}},
                "$set": {"ultima_interaccion": datetime.utcnow()}
            },
            upsert=True
        )

        return {
            "respuesta": respuesta,
            "intencion": intencion,
            "adaptada": adaptada
        }