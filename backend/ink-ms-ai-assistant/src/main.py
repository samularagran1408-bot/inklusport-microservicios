from fastapi import FastAPI

from src.api.routes import router

app = FastAPI(title="Inklusport AI Assistant")
app.include_router(router)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}
