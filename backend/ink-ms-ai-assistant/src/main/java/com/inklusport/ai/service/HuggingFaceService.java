package com.inklusport.ai.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HuggingFaceService {

    private final RestTemplate huggingFaceRestTemplate;

    @Value("${huggingface.api.key:}")
    private String apiKey;

    @Value("${huggingface.api.model:meta-llama/Llama-3.1-8B-Instruct:fastest}")
    private String model;

    @Value("${huggingface.api.temperature:0.7}")
    private float temperature;

    @Value("${huggingface.api.max-tokens:1024}")
    private int maxTokens;

    @Value("${huggingface.api.base-url:https://router.huggingface.co/v1}")
    private String baseUrl;

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
            log.warn("HUGGINGFACE_API_KEY no configurada. El chatbot no podrá conectar con Hugging Face.");
        } else if (!apiKey.startsWith("hf_")) {
            log.warn("HUGGINGFACE_API_KEY no parece válida (debe empezar con 'hf_'). Obtén una en https://huggingface.co/settings/tokens");
        }
    }

    public boolean isConfigured() {
        return StringUtils.hasText(apiKey) && apiKey.startsWith("hf_");
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
            log.error("Hugging Face no configurado: falta HUGGINGFACE_API_KEY válida");
            return "El servicio de IA no está configurado. Contacta al administrador para configurar HUGGINGFACE_API_KEY.";
        }

        try {
            log.info("Enviando petición a Hugging Face (modelo: {})...", model);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", userMessage)
                    ),
                    "temperature", temperature,
                    "max_tokens", maxTokens,
                    "stream", false
            );

            ResponseEntity<ChatCompletionResponse> response = huggingFaceRestTemplate.postForEntity(
                    baseUrl + "/chat/completions",
                    new HttpEntity<>(body, headers),
                    ChatCompletionResponse.class
            );

            String result = extractContent(response.getBody());
            if (!StringUtils.hasText(result)) {
                log.error("Hugging Face devolvió respuesta vacía");
                return "Lo siento, no pude generar una respuesta. Por favor, intenta de nuevo.";
            }

            log.info("Respuesta generada ({} caracteres)", result.length());
            return result;

        } catch (RestClientException e) {
            log.error("Error en Hugging Face: {}", e.getMessage(), e);
            return "Lo siento, ocurrió un error al procesar tu mensaje. Por favor, intenta de nuevo.";
        }
    }

    private String extractContent(ChatCompletionResponse response) {
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return null;
        }
        ChatChoice choice = response.getChoices().get(0);
        if (choice.getMessage() == null) {
            return null;
        }
        return choice.getMessage().getContent();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ChatCompletionResponse {
        private List<ChatChoice> choices;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ChatChoice {
        private ChatMessage message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ChatMessage {
        private String role;
        private String content;
    }
}
