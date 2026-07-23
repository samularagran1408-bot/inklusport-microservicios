import httpx
from app.config import settings

class UserService:
    def __init__(self):
        self.base_url = settings.USERS_SERVICE_URL

    async def get_user_profile(self, user_id: str):
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{self.base_url}/api/users/profile/{user_id}")
            return response.json() if response.status_code == 200 else {}