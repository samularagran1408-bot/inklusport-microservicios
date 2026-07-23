from app.services.grok_service import GrokService
from app.services.user_service import UserService
from app.services.sports_service import SportsService
import json
import re

class RecomendacionAgent:
    def __init__(self):
        self.grok = GrokService()
        self.user_service = UserService()
        self.sports_service = SportsService()

    async def recomendar_eventos(self, usuario_id: str):
        user_data = await self.user_service.get_user_profile(usuario_id)
        eventos = await self.sports_service.get_eventos_activos()

        prompt = f"""
        Recomienda eventos para un usuario con:
        - Nombre: {user_data.get('fullName', 'Usuario')}
        - Discapacidad: {user_data.get('disability', 'general')}
        - Eventos disponibles: {len(eventos)}

        Recomienda los 3 eventos más adecuados.

        Entregar en formato JSON:
        {{
            "recomendaciones": [
                {{"evento": "Evento 1", "razon": "Razón"}}
            ],
            "mensaje": "Mensaje personalizado"
        }}
        """

        respuesta = await self.grok.chat(prompt, user_data.get('disability', 'general'))

        try:
            json_match = re.search(r'\{.*\}', respuesta, re.DOTALL)
            if json_match:
                return json.loads(json_match.group())
        except:
            pass

        return {
            "recomendaciones": [
                {"evento": "Evento de prueba", "razon": "Ideal para tu nivel"}
            ],
            "mensaje": "Próximamente tendremos eventos disponibles para ti"
        }