from pydantic import BaseModel


class AgentResponse(BaseModel):
    response: str
    status: str = "ok"
