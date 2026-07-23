import httpx
from app.config import settings

class SportsService:
    def __init__(self):
        self.base_url = settings.SPORTS_SERVICE_URL

    async def get_eventos_activos(self):
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{self.base_url}/api/events/active")
            return response.json() if response.status_code == 200 else []

    # 👇 AGREGAR ESTE MÉTODO
    async def get_eventos_usuario(self, usuario_id: str):
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{self.base_url}/api/events/usuario/{usuario_id}")
            return response.json() if response.status_code == 200 else []