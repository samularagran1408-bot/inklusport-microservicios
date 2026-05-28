package com.inklusport.ia.service;

import org.springframework.stereotype.Service;

/**
 * Respuestas simples del chatbot sin LLM (MVP).
 * Cuando ia.enabled=true y exista un LlmClient, se puede llamar desde ChatbotServiceImpl.
 */
@Service
public class ChatbotRespuestaService {

    public String respuestaPorIntencion(String intencion) {
        return switch (intencion) {
            case "SALUDO" -> "Hola! Soy el asistente de InkluSport. En que te puedo ayudar hoy?";
            case "AYUDA" -> "Puedo orientarte con tu plan, tu progreso o dudas del entrenamiento inclusivo. Escribe lo que necesites.";
            case "PROGRESO" -> "Para ver tu avance revisa tus analisis biomecanicos y el plan que te asigno tu entrenador.";
            case "CIERRE" -> "Gracias por escribir. Cuando quieras seguimos. Exito en tu entrenamiento!";
            default -> "Entiendo. Cuentame un poco mas y te ayudo con informacion de InkluSport.";
        };
    }
}
