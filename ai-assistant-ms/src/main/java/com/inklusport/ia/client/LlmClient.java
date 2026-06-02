package com.inklusport.ia.client;

/**
 * Contrato para cuando integren un LLM de verdad.
 * Hoy no hay implementacion activa: el chat usa respuestas fijas en ChatbotRespuestaService.
 */
public interface LlmClient {

    String completar(String prompt);
}
