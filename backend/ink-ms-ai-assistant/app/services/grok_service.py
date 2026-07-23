import httpx
from app.config import settings

class GrokService:
    def __init__(self):
        self.api_key = settings.GROK_API_KEY
        self.model = settings.GROK_MODEL
        self.api_url = settings.GROK_API_URL

    async def chat(self, prompt: str, disability_type: str = "visual") -> str:
        disability_context = {
            "visual": "Responde con descripciones claras. Evita referencias visuales como 'mira', 'observa'.",
            "auditiva": "Usa lenguaje claro y directo. Evita referencias sonoras.",
            "cognitiva": "Usa frases cortas, lenguaje simple. Organiza la información en pasos.",
            "motriz": "Enfócate en adaptaciones físicas y accesibilidad.",
            "multiple": "Combina todas las adaptaciones según sea necesario."
        }

        system_prompt = f"""Eres el asistente virtual de InkluSport, una plataforma de deporte inclusivo.
        El usuario tiene discapacidad: {disability_type}
        Instrucciones:
        1. Sé empático, claro y profesional.
        2. Adapta tu respuesta a su discapacidad.
        3. Si no sabes algo, indícalo amablemente.
        4. Responde en español.
        Contexto: {disability_context.get(disability_type, "Sin adaptación específica")}
        """

        try:
            headers = {
                "Authorization": f"Bearer {self.api_key}",
                "Content-Type": "application/json"
            }

            payload = {
                "messages": [
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": prompt}
                ],
                "model": self.model,
                "temperature": 0.7,
                "max_tokens": 1024
            }

            async with httpx.AsyncClient(timeout=60.0) as client:
                response = await client.post(self.api_url, headers=headers, json=payload)
                response.raise_for_status()
                data = response.json()
                return data["choices"][0]["message"]["content"]

        except Exception as e:
            return f"Error al procesar tu mensaje: {str(e)}"