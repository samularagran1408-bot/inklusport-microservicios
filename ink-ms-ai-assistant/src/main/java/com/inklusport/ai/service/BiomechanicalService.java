package com.inklusport.ai.service;

import com.inklusport.ai.dto.BiomechanicalRequest;
import com.inklusport.ai.dto.BiomechanicalResponse;
import com.inklusport.ai.model.BiomechanicalAnalysis;
import com.inklusport.ai.repository.BiomechanicalAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiomechanicalService {

    private final BiomechanicalAnalysisRepository analysisRepository;

    public BiomechanicalResponse analyzeMovement(String userId, BiomechanicalRequest request) {
        Map<String, Object> movementData = request.getMovementData();
        
        /**
         * Calcular métricas (ejemplo con datos simulados)
         */
        Double rangoMovimiento = extractDouble(movementData, "rangeOfMotion", 70.0);
        Double simetria = extractDouble(movementData, "symmetry", 85.0);
        Double estabilidad = extractDouble(movementData, "stability", 80.0);
        
        /**
         * Fórmula: (rango_movimiento * 0.4) + (simetria * 0.3) + (estabilidad * 0.3)
         */
        Double puntajeGeneral = (rangoMovimiento * 0.4) + (simetria * 0.3) + (estabilidad * 0.3);
        
        /**
         * Determinar nivel de fatiga
         */
        String fatigueLevel = determineFatigueLevel(puntajeGeneral);
        
        /**
         * Generar recomendaciones
         */
        String recommendations = generateRecommendations(puntajeGeneral, fatigueLevel);
        
        /**
         * Guardar análisis
         */
        BiomechanicalAnalysis analysis = BiomechanicalAnalysis.builder()
                .usuarioId(userId)
                .ejercicioNombre(request.getEjercicioNombre())
                .disabilityType(request.getDisabilityType())
                .fecha(LocalDateTime.now())
                .rangoMovimiento(rangoMovimiento)
                .simetria(simetria)
                .estabilidad(estabilidad)
                .puntajeGeneral(puntajeGeneral)
                .recomendaciones(recommendations)
                .sessionId(UUID.randomUUID().toString())
                .build();
        
        analysisRepository.save(analysis);
        log.info("Análisis biomecánico guardado para usuario: {}", userId);
        
        return BiomechanicalResponse.builder()
                .analysisId(analysis.getId())
                .rangeOfMotion(rangoMovimiento)
                .symmetry(simetria)
                .stability(estabilidad)
                .generalScore(puntajeGeneral)
                .fatigueLevel(fatigueLevel)
                .recommendations(recommendations)
                .build();
    }
    
    /**
     * Extraer valores numéricos de un mapa con seguridad
     */
    private Double extractDouble(Map<String, Object> data, String key, Double defaultValue) {
        if (data != null && data.containsKey(key)) {
            Object value = data.get(key);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }
        return defaultValue;
    }
    
    /**
     * Determinar nivel de fatiga basado en el puntaje general
     */
    private String determineFatigueLevel(Double score) {
        if (score >= 85) return "bajo";
        if (score >= 60) return "medio";
        return "alto";
    }
    
    /**
     * Generar recomendaciones basadas en el nivel de fatiga
     */
    private String generateRecommendations(Double score, String fatigueLevel) {
        if ("alto".equals(fatigueLevel)) {
            return "Se recomienda descanso inmediato. Realizar pausas cada 10 minutos.";
        } else if ("medio".equals(fatigueLevel)) {
            return "Mantener el ritmo. Realizar pausas cada 20 minutos.";
        }
        return "Excelente rendimiento. Continuar con el plan actual.";
    }
}