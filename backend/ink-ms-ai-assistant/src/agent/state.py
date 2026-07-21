"""State definitions for the assistant workflow."""

from dataclasses import dataclass


@dataclass
class AgentState:
    message: str = ""
    context: dict | None = None
