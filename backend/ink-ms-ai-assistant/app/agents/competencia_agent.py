from app.services.grok_service import GrokService
from app.services.sports_service import SportsService
import json
import re

class CompetenciaAgent:
    def __init__(self):
        self.grok = GrokService()
        self.sports_service = SportsService()

    async def analizar_rendimiento(self, usuario_id: str):
        # 👇 USAR EL MÉTODO CORRECTO
        eventos = await self.sports_service.get_eventos_usuario(usuario_id)
        
        prompt = f"""
        Analiza el rendimiento del usuario (ID: {usuario_id}) en la plataforma.
        Eventos participados: {len(eventos)}

        Genera un informe con:
        1. Estadísticas principales
        2. Ventajas (fortalezas)
        3. Desventajas (áreas de mejora)
        4. Recomendaciones

        Entregar en formato JSON:
        {{
            "estadisticas": {{"total_eventos": 0}},
            "ventajas": ["ventaja1", "ventaja2"],
            "desventajas": ["desventaja1"],
            "recomendaciones": ["recomendacion1"]
        }}
        """

        respuesta = await self.grok.chat(prompt, "general")

        try:
            json_match = re.search(r'\{.*\}', respuesta, re.DOTALL)
            if json_match:
                return json.loads(json_match.group())
        except:
            pass

        return {
            "estadisticas": {"total_eventos": len(eventos)},
            "ventajas": ["Buena participación"],
            "desventajas": [],
            "recomendaciones": ["Continúa participando"]
        }