from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.database.mongodb import connect_to_mongo, close_mongo_connection
from app.routers import chat, rutinas, competencia, recomendacion

app = FastAPI(
    title="InkluSport AI Assistant",
    description="Agente de IA con Grok para InkluSport",
    version="1.0.0"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
async def startup():
    await connect_to_mongo()
    print("Conectado a MongoDB")

@app.on_event("shutdown")
async def shutdown():
    await close_mongo_connection()
    print("Desconectado de MongoDB")

# Routers
app.include_router(chat.router, prefix="/api/ai/chat", tags=["Chatbot"])
app.include_router(rutinas.router, prefix="/api/ai/rutinas", tags=["Rutinas"])
app.include_router(competencia.router, prefix="/api/ai/competencia", tags=["Competencia"])
app.include_router(recomendacion.router, prefix="/api/ai/recomendacion", tags=["Recomendaciones"])

@app.get("/api/ai/health")
async def health_check():
    return {
        "status": "healthy",
        "service": "ink-ms-ai-assistant",
        "llm": "Grok"
    }