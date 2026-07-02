package com.inklusport.ai.service;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-2.0-flash}")
    private String model;

    private static final String SYSTEM_PROMPT = """
            Eres un asistente virtual de Inklusport, una plataforma de deportes adaptados.
            
            CARACTERÍSTICAS:
            - Hablas de manera clara, amable y empática
            - Usas lenguaje inclusivo
            - Respondes solo sobre temas relacionados con deportes adaptados
            - Si te preguntan algo fuera del tema, rediriges amablemente
            - Das respuestas concisas pero completas
            """;

    public String getAIResponse(String userMessage) {
        try {
            log.info("Enviando petición a Google Gemini...");

            /**
             * 1. Crear cliente
             */
            Client client = Client.builder()
                    .apiKey(apiKey)
                    .build();

            /**
             * 2. Construir mensajes
             */
            List<Content> contents = List.of(
                Content.builder()
                    .role("user")
                    .parts(List.of(
                        Part.builder().text(SYSTEM_PROMPT).build(),
                        Part.builder().text(userMessage).build()
                    ))
                    .build()
            );

            /**
             * 3. Enviar petición
             */
            GenerateContentResponse response = client.models.generateContent(
                model,
                contents,
                null
            );

            /**
             * 4. Extraer respuesta
             */
            String result = response.text();
            log.info("Respuesta generada ({} caracteres)", result.length());
            return result;

        } catch (Exception e) {
            log.error("Error en Gemini: {}", e.getMessage(), e);
            return "Lo siento, ocurrió un error al procesar tu mensaje. Por favor, intenta de nuevo.";
        }
    }
}