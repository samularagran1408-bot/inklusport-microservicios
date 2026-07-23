from app.services.grok_service import GrokService
from app.services.user_service import UserService
import json
import re

class RutinasAgent:
    def __init__(self):
        self.grok = GrokService()
        self.user_service = UserService()

    async def generar_rutina(self, usuario_id: str, tipo: str, objetivo: str, discapacidad: str):
        user_data = await self.user_service.get_user_profile(usuario_id)

        prompt = f"""
        Genera una rutina de ejercicio profesional para un usuario con:
        - Nombre: {user_data.get('fullName', 'Usuario')}
        - Discapacidad: {discapacidad}
        - Objetivo: {objetivo}
        - Tipo: {tipo}

        La rutina debe incluir 6-8 ejercicios con: nombre, repeticiones, series, tiempo estimado, adaptaciones, esfuerzo (1-5).

        Entregar en formato JSON:
        {{
            "nombre": "Rutina personalizada",
            "ejercicios": [
                {{
                    "nombre": "Ejercicio 1",
                    "repeticiones": 12,
                    "series": 3,
                    "tiempo_estimado": 45,
                    "esfuerzo": 3,
                    "descanso": 60
                }}
            ],
            "objetivo": "Descripción del objetivo",
            "recomendaciones": "Recomendaciones para la ejecución"
        }}
        """

        respuesta = await self.grok.chat(prompt, discapacidad)

        try:
            json_match = re.search(r'\{.*\}', respuesta, re.DOTALL)
            if json_match:
                return json.loads(json_match.group())
        except:
            pass

        return {
            "nombre": f"Rutina {tipo} para {discapacidad}",
            "ejercicios": [
                {"nombre": "Ejercicio 1", "repeticiones": 10, "series": 3, "tiempo_estimado": 30, "esfuerzo": 3, "descanso": 60},
                {"nombre": "Ejercicio 2", "repeticiones": 8, "series": 3, "tiempo_estimado": 30, "esfuerzo": 3, "descanso": 60}
            ],
            "objetivo": objetivo,
            "recomendaciones": "Consulta a tu entrenador para una rutina personalizada"
        }