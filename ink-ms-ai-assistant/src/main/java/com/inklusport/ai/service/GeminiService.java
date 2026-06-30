package com.inklusport.ai.service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.inklusport.ai.model.Mensaje;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private final Client geminiClient;

    @Value("${gemini.api.model:gemini-2.0-flash}")
    private String model;

    @Value("${gemini.api.temperature:0.7}")
    private float temperature;

    @Value("${gemini.api.max-tokens:500}")
    private int maxTokens;

    private static final String SYSTEM_PROMPT = """
            Eres un asistente virtual de Inklusport, una plataforma de deportes adaptados.
            
            CARACTERÍSTICAS:
            - Hablas de manera clara, amable y empática
            - Usas lenguaje inclusivo
            - Respondes solo sobre temas relacionados con deportes adaptados
            - Si te preguntan algo fuera del tema, rediriges amablemente
            - Das respuestas concisas pero completas
            - Si necesitas más información para ayudar, la solicitas amablemente
            
            CONTEXTO DE INKLUSPORT:
            - Deportes: Natación Adaptada, Baloncesto en Silla, Yoga Inclusivo, Atletismo Adaptado
            - Eventos: Clínicas, Torneos, Talleres, Jornadas
            - Inscripciones: A través del calendario de la plataforma
            - Adaptaciones: Visuales, Auditivas, Cognitivas, Motrices
            """;

    public String getAIResponse(String userMessage, List<Mensaje> historial) {
        try {
            log.info("Enviando petición a Google Gemini (modelo: {})...", model);

            List<Content> contents = buildContents(historial, userMessage);

            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText(SYSTEM_PROMPT)))
                    .temperature(temperature)
                    .maxOutputTokens(maxTokens)
                    .build();

            GenerateContentResponse response = geminiClient.models.generateContent(model, contents, config);
            String result = response.text();

            if (result == null || result.isBlank()) {
                log.warn("Gemini devolvió respuesta vacía");
                return "Lo siento, no pude generar una respuesta. Por favor, intenta de nuevo.";
            }

            log.info("Respuesta generada ({} caracteres)", result.length());
            return result;

        } catch (Exception e) {
            log.error("Error en Gemini: {}", e.getMessage(), e);
            return "Lo siento, ocurrió un error al procesar tu mensaje. Por favor, intenta de nuevo.";
        }
    }

    private List<Content> buildContents(List<Mensaje> historial, String userMessage) {
        List<Content> contents = new ArrayList<>();

        int start = Math.max(0, historial.size() - 10);
        for (int i = start; i < historial.size(); i++) {
            Mensaje msg = historial.get(i);
            String role = "usuario".equals(msg.getRemitente()) ? "user" : "model";
            contents.add(Content.builder()
                    .role(role)
                    .parts(List.of(Part.fromText(msg.getMensaje())))
                    .build());
        }

        if (contents.isEmpty()
                || historial.isEmpty()
                || !userMessage.equals(historial.get(historial.size() - 1).getMensaje())) {
            contents.add(Content.fromParts(Part.fromText(userMessage)));
        }

        return contents;
    }
}
