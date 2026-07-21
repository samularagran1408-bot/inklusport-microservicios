from fastapi import APIRouter

from src.agent.orchestrator import orchestrate
from src.models.agent_request import AgentRequest
from src.models.agent_response import AgentResponse

router = APIRouter(prefix="/ai-assistant", tags=["assistant"])


@router.post("/chat", response_model=AgentResponse)
def chat(request: AgentRequest) -> AgentResponse:
    return AgentResponse(response=orchestrate(request.message))
