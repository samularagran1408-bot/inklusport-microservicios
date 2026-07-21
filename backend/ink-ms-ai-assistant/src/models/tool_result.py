from pydantic import BaseModel


class ToolResult(BaseModel):
    tool: str
    result: str
