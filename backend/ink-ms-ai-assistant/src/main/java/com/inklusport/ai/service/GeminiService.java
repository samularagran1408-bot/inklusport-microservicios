package com.inklusport.ai.service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {

    private final Client geminiClient;

    @Value("${gemini.api.model:gemini-2.0-flash}")
    private String model;

    @Value("${gemini.api.temperature:0.7}")
    private float temperature;

    @Value("${gemini.api.max-tokens:1024}")
    private int maxTokens;

    @Value("${gemini.api.key:}")
    private String apiKey;

    private static final String DEFAULT_SYSTEM_PROMPT = """
            Eres un asistente virtual de Inklusport, una plataforma de deportes adaptados.

            CARACTERÍSTICAS:
            - Hablas de manera clara, amable y empática
            - Usas lenguaje inclusivo
            - Respondes solo sobre temas relacionados con deportes adaptados
            - Si te preguntan algo fuera del tema, rediriges amablemente
            - Das respuestas concisas pero completas
            """;

    @PostConstruct
    void validateConfiguration() {
        if (!StringUtils.hasText(apiKey)) {
            log.warn("GEMINI_API_KEY no configurada. El chatbot no podrá conectar con Gemini.");
        } else if (!apiKey.startsWith("AIza")) {
            log.warn("GEMINI_API_KEY no parece válida (debe empezar con 'AIza'). Obtén una en https://aistudio.google.com/apikey");
        }
    }

    public boolean isConfigured() {
        return StringUtils.hasText(apiKey) && apiKey.startsWith("AIza");
    }

    public String getAIResponse(String userMessage) {
        return generate(DEFAULT_SYSTEM_PROMPT, userMessage);
    }

    public String getAIResponseWithHistory(String userMessage, String conversationHistory) {
        String prompt = conversationHistory.isBlank()
                ? userMessage
                : "Historial de conversación:\n" + conversationHistory + "\n\nNuevo mensaje del usuario: " + userMessage;
        return generate(DEFAULT_SYSTEM_PROMPT, prompt);
    }

    public String generate(String systemPrompt, String userMessage) {
        if (!isConfigured()) {
            log.error("Gemini no configurado: falta GEMINI_API_KEY válida");
            return "El servicio de IA no está configurado. Contacta al administrador para configurar GEMINI_API_KEY.";
        }

        try {
            log.info("Enviando petición a Google Gemini (modelo: {})...", model);

            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText(systemPrompt)))
                    .temperature(temperature)
                    .maxOutputTokens(maxTokens)
                    .build();

            GenerateContentResponse response = geminiClient.models.generateContent(
                    model,
                    userMessage,
                    config
            );

            String result = response.text();
            if (!StringUtils.hasText(result)) {
                log.error("Gemini devolvió respuesta vacía");
                return "Lo siento, no pude generar una respuesta. Por favor, intenta de nuevo.";
            }

            log.info("Respuesta generada ({} caracteres)", result.length());
            return result;

        } catch (Exception e) {
            log.error("Error en Gemini: {}", e.getMessage(), e);
            return "Lo siento, ocurrió un error al procesar tu mensaje. Por favor, intenta de nuevo.";
        }
    }
}
