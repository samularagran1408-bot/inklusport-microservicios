package com.inklusport.ai.service;

import com.inklusport.ai.model.ChatTraining;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdaptiveResponseService {

    /**
     * Obtener respuesta adaptada según tipo de discapacidad
     */
    public String getAdaptedResponse(ChatTraining training, String disabilityType) {
        if (training == null) {
            return "Lo siento, no encontré una respuesta adecuada.";
        }

        /**
         * Validar tipo de discapacidad
         */
        if (disabilityType == null || disabilityType.isEmpty() || 
            "no_especificado".equals(disabilityType)) {
            return training.getRespuestaBase();
        }

        /**
         * Buscar respuesta adaptada
         */
        Map<String, String> adaptada = training.getRespuestaAdaptada();
        if (adaptada != null && adaptada.containsKey(disabilityType)) {
            String response = adaptada.get(disabilityType);
            if (response != null && !response.isEmpty()) {
                log.info("Usando respuesta adaptada para discapacidad: {}", disabilityType);
                
                /**
                 * Aplicar mejoras adicionales según el tipo
                 */
                return applyAdditionalAdaptations(response, disabilityType);
            }
        }

        /**
         * Fallback: si no hay adaptada, usar respuesta base
         */
        log.info("Usando respuesta base (sin adaptación para: {})", disabilityType);
        return training.getRespuestaBase();
    }

    /**
     * Aplicar adaptaciones adicionales según tipo de discapacidad
     */
    private String applyAdditionalAdaptations(String response, String disabilityType) {
        if (response == null) return null;

        switch (disabilityType.toLowerCase()) {
            case "visual":
                /**
                 * Respuesta optimizada para lector de pantalla
                 */
                return response.replace("*", " ")
                              .replace("-", " ")
                              .replaceAll("\\s+", " ");
                
            case "auditiva":
                /**
                 * Respuesta sin elementos sonoros
                 */
                return response.replaceAll("[!¡]", ".")
                              .replaceAll("[?¿]", ".");
                
            case "cognitiva":
                /**
                 * Lenguaje simplificado
                 */
                return simplifyLanguage(response);
                
            case "motriz":
                /**
                 * Respuesta con instrucciones claras
                 */
                return response.replaceAll("\\. ", ". Paso: ");
                
            default:
                return response;
        }
    }

    /**
     * Simplificar lenguaje para discapacidad cognitiva
     */
    private String simplifyLanguage(String text) {
        if (text == null) return null;
        
        /**
         * Dividir en oraciones
         */
        String[] sentences = text.split("\\.");
        StringBuilder simplified = new StringBuilder();
        
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (!sentence.isEmpty()) {
                /**
                 * Simplificar oraciones largas
                 */
                if (sentence.split("\\s+").length > 15) {
                    String[] words = sentence.split("\\s+");
                    simplified.append(words[0]).append(" ");
                    // Tomar solo palabras clave
                    for (int i = 1; i < words.length; i++) {
                        if (isKeyWord(words[i])) {
                            simplified.append(words[i]).append(" ");
                        }
                    }
                    simplified.append(". ");
                } else {
                    simplified.append(sentence).append(". ");
                }
            }
        }
        
        return simplified.toString().trim();
    }

    /**
     * Verificar si una palabra es clave
     */
    private boolean isKeyWord(String word) {
        String[] stopWords = {"el", "la", "los", "las", "un", "una", "y", "o", "pero", "porque"};
        word = word.toLowerCase();
        for (String stop : stopWords) {
            if (word.equals(stop)) {
                return false;
            }
        }
        return word.length() > 3;
    }

    /**
     * Generar respuesta adaptada para mensaje de error
     */
    public String getErrorResponse(String disabilityType) {
        String baseResponse = "Lo siento, ocurrió un error al procesar tu mensaje. ¿Puedes intentarlo de nuevo?";
        
        if (disabilityType == null || "no_especificado".equals(disabilityType)) {
            return baseResponse;
        }
        
        return applyAdditionalAdaptations(baseResponse, disabilityType);
    }
}