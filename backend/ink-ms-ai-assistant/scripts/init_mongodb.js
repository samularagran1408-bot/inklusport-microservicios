db = db.getSiblingDB("inclusport_training_ia");

db.createCollection("conversaciones_chatbot");
db.createCollection("entrenamiento_chatbot");

db.entrenamiento_chatbot.insertMany([
    {
        pregunta: "Hola",
        respuesta_base: "¡Hola! Soy el asistente virtual de InkluSport. ¿En qué puedo ayudarte?",
        intencion: "saludo",
        palabras_clave: ["hola", "buenos", "saludo"],
        respuesta_adaptada: {
            visual: "Hola. Soy el asistente de InkluSport. ¿Cómo puedo ayudarte?",
            auditiva: "¡Hola! Soy tu asistente. ¿Qué necesitas?",
            cognitiva: "Hola. Soy tu asistente. Dime cómo te ayudo."
        },
        activo: true
    }
]);

print("Base de datos inicializada");