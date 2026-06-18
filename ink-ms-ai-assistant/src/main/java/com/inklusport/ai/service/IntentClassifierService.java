package com.inklusport.ai.service;

import com.inklusport.ai.model.ChatTraining;
import com.inklusport.ai.repository.ChatTrainingRepository;
import com.inklusport.ai.util.TextProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntentClassifierService {

    private final ChatTrainingRepository chatTrainingRepository;
    private final TextProcessor textProcessor;

    /**
     * Mapa de palabras clave a intenciones (fallback)
     */
    private static final Map<String, String> KEYWORD_TO_INTENT = new HashMap<>();
    
    /**
     * Cache de clasificación
     */
    private final Map<String, String> classificationCache = new ConcurrentHashMap<>();

    static {
        KEYWORD_TO_INTENT.put("hola", "saludo");
        KEYWORD_TO_INTENT.put("buenos dias", "saludo");
        KEYWORD_TO_INTENT.put("buenas tardes", "saludo");
        KEYWORD_TO_INTENT.put("eventos", "eventos");
        KEYWORD_TO_INTENT.put("actividades", "eventos");
        KEYWORD_TO_INTENT.put("inscribir", "inscripcion");
        KEYWORD_TO_INTENT.put("registrar", "inscripcion");
        KEYWORD_TO_INTENT.put("apuntarme", "inscripcion");
        KEYWORD_TO_INTENT.put("participar", "inscripcion");
        KEYWORD_TO_INTENT.put("deportes", "deportes");
        KEYWORD_TO_INTENT.put("ejercicios", "deportes");
        KEYWORD_TO_INTENT.put("progreso", "progreso");
        KEYWORD_TO_INTENT.put("avance", "progreso");
        KEYWORD_TO_INTENT.put("entrenamiento", "entrenamiento");
        KEYWORD_TO_INTENT.put("rutina", "entrenamiento");
        KEYWORD_TO_INTENT.put("plan", "entrenamiento");
        KEYWORD_TO_INTENT.put("ayuda", "ayuda");
        KEYWORD_TO_INTENT.put("soporte", "ayuda");
        KEYWORD_TO_INTENT.put("adios", "despedida");
        KEYWORD_TO_INTENT.put("chao", "despedida");
        KEYWORD_TO_INTENT.put("hasta luego", "despedida");
        KEYWORD_TO_INTENT.put("perfil", "perfil");
        KEYWORD_TO_INTENT.put("mi cuenta", "perfil");
    }

    /**
     * Clasificar intención del mensaje con caché
     */
    @Cacheable(value = "intentClassifications", key = "#message")
    public String classifyIntent(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "ayuda";
        }

        String messageLower = message.toLowerCase().trim();
        
        /**
         * Verificar caché en memoria
         */
        if (classificationCache.containsKey(messageLower)) {
            log.debug("Clasificación desde caché: '{}' -> {}", message, classificationCache.get(messageLower));
            return classificationCache.get(messageLower);
        }

        String intent = classifyWithPriority(messageLower);
        
        /**
         * Guardar en caché
         */
        classificationCache.put(messageLower, intent);
        
        log.info("Mensaje: '{}' -> Intención: {}", message, intent);
        return intent;
    }

    /**
     * Clasificación con prioridad
     */
    private String classifyWithPriority(String message) {
        /**
         * 1. Buscar en entrenamiento
         */
        List<ChatTraining> trainings = chatTrainingRepository.findByActivoTrue();
        
        /**
         * Ordenar por prioridad (mayor primero)
         */
        trainings.sort((t1, t2) -> {
            int p1 = t1.getPrioridad() != null ? t1.getPrioridad() : 0;
            int p2 = t2.getPrioridad() != null ? t2.getPrioridad() : 0;
            return Integer.compare(p2, p1);
        });

        /**
         * Buscar coincidencia exacta primero
         */
        for (ChatTraining training : trainings) {
            List<String> keywords = training.getPalabrasClave();
            if (keywords != null) {
                for (String keyword : keywords) {
                    if (keyword != null && message.contains(keyword.toLowerCase())) {
                        log.debug("Intención detectada por palabra clave: {} -> {}", keyword, training.getIntencion());
                        return training.getIntencion();
                    }
                }
            }
        }

        /**
         * 2. Buscar por frase completa (similitud)
         */
        for (ChatTraining training : trainings) {
            String pregunta = training.getPregunta();
            if (pregunta != null && !pregunta.isEmpty()) {
                double similarity = calculateSimilarity(message, pregunta.toLowerCase());
                if (similarity > 0.7) {
                    log.debug("Intención detectada por similitud: {} -> {}", pregunta, training.getIntencion());
                    return training.getIntencion();
                }
            }
        }

        /**
         * 3. Fallback: palabras clave simples
         */
        for (Map.Entry<String, String> entry : KEYWORD_TO_INTENT.entrySet()) {
            if (message.contains(entry.getKey())) {
                log.debug("Intención detectada (fallback): {} -> {}", entry.getKey(), entry.getValue());
                return entry.getValue();
            }
        }

        /**
         * 4. Intención por defecto
         */
        log.debug("Intención no detectada, usando default: ayuda");
        return "ayuda";
    }

    /**
     * Calcular similitud entre dos textos
     */
    private double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        
        /**
         * Convertir a conjuntos de palabras
         */
        Set<String> words1 = new HashSet<>(Arrays.asList(s1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(s2.split("\\s+")));
        
        /**
         * Calcular intersección
         */
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);
        
        /**
         * Calcular unión
         */
        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        
        if (union.isEmpty()) return 0.0;
        
        return (double) intersection.size() / union.size();
    }

    /**
     * Obtener todas las intenciones disponibles
     */
    public List<String> getAllIntents() {
        return chatTrainingRepository.findByActivoTrue().stream()
                .map(ChatTraining::getIntencion)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Limpiar caché de clasificación
     */
    public void clearCache() {
        classificationCache.clear();
        log.info("🧹 Caché de clasificación limpiado");
    }
}