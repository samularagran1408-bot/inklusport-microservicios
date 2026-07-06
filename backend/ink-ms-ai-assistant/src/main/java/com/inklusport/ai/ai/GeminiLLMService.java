package com.inklusport.ai.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiLLMService {

    public String getAIResponse(String prompt) {
        log.info("Consultando Gemini para prompt: {}", prompt);
        return "Respuesta generada por Gemini para: " + prompt;
    }

    public String getAIResponseWithHistory(String prompt, String history) {
        log.info("Consultando Gemini con historial para prompt: {}", prompt);
        return "Respuesta generada por Gemini con contexto: " + prompt;
    }
}
