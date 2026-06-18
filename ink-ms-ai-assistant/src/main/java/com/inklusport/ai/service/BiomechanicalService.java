package com.inklusport.ai.service;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.BiomechanicalRequest;
import com.inklusport.ai.dto.BiomechanicalResponse;
import com.inklusport.ai.exception.CustomExceptions;
import com.inklusport.ai.model.BiomechanicalAnalysis;
import com.inklusport.ai.repository.BiomechanicalAnalysisRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiomechanicalService {

    private final BiomechanicalAnalysisRepository biomechanicalRepository;

    /**
     * Analizar biomecánica
     */
    @Transactional
    @CircuitBreaker(name = "biomechanicalService", fallbackMethod = "analyzeFallback")
    public BiomechanicalResponse analyze(BiomechanicalRequest request) {
        log.info("Analizando biomecánica para usuario: {}", request.getUsuarioId());

        /**
         * Validar datos
         */
        validateBiomechanicalData(request);

        /**
         * Calcular puntaje general
         */
        Double puntajeGeneral = calculateGeneralScore(request);
        
        /**
         * Determinar niveles
         */
        Map<String, String> niveles = determineLevels(request, puntajeGeneral);
        
        /**
         * Generar recomendaciones
         */
        List<String> recomendaciones = generateDetailedRecommendations(request, puntajeGeneral);

        /**
         * Guardar análisis
         */
        BiomechanicalAnalysis analysis = saveAnalysis(request, puntajeGeneral, recomendaciones);

        /**
         * Construir respuesta
         */
        return buildResponse(analysis, niveles, recomendaciones);
    }

    /**
     * Fallback para análisis
     */
    public BiomechanicalResponse analyzeFallback(BiomechanicalRequest request, Throwable ex) {
        log.warn("Circuit Breaker activado para análisis biomecánico");
        
        return BiomechanicalResponse.builder()
                .usuarioId(request.getUsuarioId())
                .puntajeGeneral(50.0)
                .recomendaciones("Servicio temporalmente no disponible. Usando valores estimados.")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Validar datos biomecánicos
     */
    private void validateBiomechanicalData(BiomechanicalRequest request) {
        if (request.getRangoMovimiento() < 0 || request.getRangoMovimiento() > 100) {
            throw new CustomExceptions.InvalidRequestException("Rango de movimiento debe ser entre 0 y 100");
        }
        if (request.getSimetria() < 0 || request.getSimetria() > 100) {
            throw new CustomExceptions.InvalidRequestException("Simetría debe ser entre 0 y 100");
        }
        if (request.getEstabilidad() < 0 || request.getEstabilidad() > 100) {
            throw new CustomExceptions.InvalidRequestException("Estabilidad debe ser entre 0 y 100");
        }
    }

    /**
     * Calcular puntaje general
     */
    private Double calculateGeneralScore(BiomechanicalRequest request) {
        return (request.getRangoMovimiento() * 0.4) + 
               (request.getSimetria() * 0.3) + 
               (request.getEstabilidad() * 0.3);
    }

    /**
     * Determinar niveles
     */
    private Map<String, String> determineLevels(BiomechanicalRequest request, Double puntajeGeneral) {
        Map<String, String> niveles = new HashMap<>();
        
        niveles.put("rangoMovimiento", getLevel(request.getRangoMovimiento()));
        niveles.put("simetria", getLevel(request.getSimetria()));
        niveles.put("estabilidad", getLevel(request.getEstabilidad()));
        niveles.put("general", getLevel(puntajeGeneral));
        
        return niveles;
    }

    /**
     * Obtener nivel según puntaje
     */
    private String getLevel(Double value) {
        if (value >= 70) return "ALTO";
        if (value >= 50) return "MEDIO";
        return "BAJO";
    }

    /**
     * Generar recomendaciones detalladas
     */
    private List<String> generateDetailedRecommendations(
            BiomechanicalRequest request, Double puntajeGeneral) {
        
        List<String> recs = new ArrayList<>();
        
        /**
         * Recomendaciones por rango de movimiento
         */
        if (request.getRangoMovimiento() < 40) {
            recs.add("Trabajar movilidad articular con ejercicios de estiramiento diarios (15 min)");
            recs.add("Realizar ejercicios de flexibilidad específicos para la articulación afectada");
        } else if (request.getRangoMovimiento() < 60) {
            recs.add("Mantener ejercicios de movilidad 3 veces por semana");
            recs.add("Incorporar ejercicios de estiramiento dinámico antes de entrenar");
        } else {
            recs.add("Buen rango de movimiento. Mantener ejercicios de mantenimiento");
        }
        
        /**
         * Recomendaciones por simetría
         */
        if (request.getSimetria() < 50) {
            recs.add("Realizar ejercicios unilaterales para mejorar simetría");
            recs.add("Trabajar lado más débil con series adicionales (2-3 más)");
        } else if (request.getSimetria() < 70) {
            recs.add("Incluir ejercicios bilaterales con enfoque en el lado más débil");
        } else {
            recs.add("Buena simetría. Continuar con ejercicios bilaterales");
        }
        
        /**
         * Recomendaciones por estabilidad
         */
        if (request.getEstabilidad() < 40) {
            recs.add("Incluir ejercicios de propiocepción y equilibrio en cada sesión");
            recs.add("Usar superficies inestables (bosu, fitball) con supervisión");
        } else if (request.getEstabilidad() < 60) {
            recs.add("Incorporar ejercicios de estabilidad 2-3 veces por semana");
        } else {
            recs.add("Buena estabilidad. Mantener ejercicios de mantenimiento");
        }
        
        /**
         * Recomendación general según puntaje
         */
        if (puntajeGeneral < 40) {
            recs.add("Puntaje bajo. Se recomienda evaluación con especialista");
            recs.add("Plan de rehabilitación intensiva recomendado");
        } else if (puntajeGeneral < 60) {
            recs.add("Plan de mejora moderado recomendado");
        } else {
            recs.add("Mantener rutina actual con progresión gradual");
        }
        
        /**
         * Recomendación específica por tipo de discapacidad
         */
        if (request.getDisabilityType() != null) {
            recs.addAll(getSpecificRecommendations(request.getDisabilityType()));
        }
        
        return recs;
    }

    /**
     * Recomendaciones específicas por tipo de discapacidad
     */
    private List<String> getSpecificRecommendations(String disabilityType) {
        List<String> recs = new ArrayList<>();
        
        switch (disabilityType.toLowerCase()) {
            case "motriz":
                recs.add("Adaptar ejercicios a la capacidad motriz del usuario");
                recs.add("Usar ayudas técnicas según necesidad");
                break;
            case "visual":
                recs.add("Proporcionar instrucciones verbales claras");
                recs.add("Usar puntos de referencia táctiles");
                break;
            case "auditiva":
                recs.add("Usar señales visuales y lenguaje de señas");
                recs.add("Aplicación con vibración para feedback");
                break;
            case "cognitiva":
                recs.add("Simplificar instrucciones paso a paso");
                recs.add("Repetir ejercicios con consistencia");
                break;
            default:
                recs.add("Plan estándar recomendado");
        }
        
        return recs;
    }

    /**
     * Guardar análisis
     */
    private BiomechanicalAnalysis saveAnalysis(
            BiomechanicalRequest request, 
            Double puntajeGeneral,
            List<String> recomendaciones) {
        
        BiomechanicalAnalysis analysis = BiomechanicalAnalysis.builder()
                .usuarioId(request.getUsuarioId())
                .planId(request.getPlanId())
                .disabilityType(request.getDisabilityType())
                .fecha(LocalDateTime.now())
                .sessionId(request.getSessionId())
                .ejercicioNombre(request.getEjercicioNombre())
                .rangoMovimiento(request.getRangoMovimiento())
                .simetria(request.getSimetria())
                .estabilidad(request.getEstabilidad())
                .puntajeGeneral(puntajeGeneral)
                .recomendaciones(String.join(". ", recomendaciones))
                .build();
        
        return biomechanicalRepository.save(analysis);
    }

    /**
     * Construir respuesta
     */
    private BiomechanicalResponse buildResponse(
            BiomechanicalAnalysis analysis,
            Map<String, String> niveles,
            List<String> recomendaciones) {
        
        return BiomechanicalResponse.builder()
                .id(analysis.getId())
                .usuarioId(analysis.getUsuarioId())
                .planId(analysis.getPlanId())
                .disabilityType(analysis.getDisabilityType())
                .puntajeGeneral(analysis.getPuntajeGeneral())
                .recomendaciones(analysis.getRecomendaciones())
                .recomendacionesDetalladas(recomendaciones)
                .rangoMovimiento(analysis.getRangoMovimiento())
                .simetria(analysis.getSimetria())
                .estabilidad(analysis.getEstabilidad())
                .nivelRangoMovimiento(niveles.get("rangoMovimiento"))
                .nivelSimetria(niveles.get("simetria"))
                .nivelEstabilidad(niveles.get("estabilidad"))
                .nivelGeneral(niveles.get("general"))
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Obtener análisis por usuario
     */
    @Cacheable(value = "biomechanicalAnalyses", key = "#usuarioId + '_' + #page + '_' + #size")
    public PaginationResponse<BiomechanicalResponse> getAnalysesByUser(
            String usuarioId, PaginationRequest pagination) {
        
        log.info("Obteniendo análisis para usuario: {}", usuarioId);
        
        PageRequest pageRequest = PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            Sort.Direction.fromString(pagination.getSortDirection()),
            pagination.getSortBy()
        );
        
        Page<BiomechanicalAnalysis> analyses = biomechanicalRepository.findByUsuarioId(
            usuarioId, pageRequest);
        
        List<BiomechanicalResponse> content = analyses.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        
        return PaginationResponse.<BiomechanicalResponse>builder()
                .content(content)
                .page(analyses.getNumber())
                .size(analyses.getSize())
                .totalElements(analyses.getTotalElements())
                .totalPages(analyses.getTotalPages())
                .first(analyses.isFirst())
                .last(analyses.isLast())
                .build();
    }

    /**
     * Obtener estadísticas de análisis
     */
    public Map<String, Object> getAnalysisStats(String usuarioId) {
        log.info("Obteniendo estadísticas de análisis para usuario: {}", usuarioId);
        
        List<BiomechanicalAnalysis> analyses = biomechanicalRepository
                .findByUsuarioId(usuarioId);
        
        if (analyses.isEmpty()) {
            return Map.of(
                "totalAnalyses", 0,
                "avgScore", 0.0,
                "minScore", 0.0,
                "maxScore", 0.0
            );
        }
        
        DoubleSummaryStatistics stats = analyses.stream()
                .mapToDouble(BiomechanicalAnalysis::getPuntajeGeneral)
                .summaryStatistics();
        
        // Contar por nivel
        long highScore = analyses.stream()
                .filter(a -> a.getPuntajeGeneral() >= 70)
                .count();
        long mediumScore = analyses.stream()
                .filter(a -> a.getPuntajeGeneral() >= 50 && a.getPuntajeGeneral() < 70)
                .count();
        long lowScore = analyses.stream()
                .filter(a -> a.getPuntajeGeneral() < 50)
                .count();
        
        return Map.of(
            "totalAnalyses", analyses.size(),
            "avgScore", stats.getAverage(),
            "minScore", stats.getMin(),
            "maxScore", stats.getMax(),
            "highScoreCount", highScore,
            "mediumScoreCount", mediumScore,
            "lowScoreCount", lowScore,
            "lastAnalysis", analyses.stream()
                .max(Comparator.comparing(BiomechanicalAnalysis::getFecha))
                .map(a -> a.getFecha().toString())
                .orElse(null)
        );
    }

    /**
     * Convertir a BiomechanicalResponse
     */
    private BiomechanicalResponse toResponse(BiomechanicalAnalysis analysis) {
        return BiomechanicalResponse.builder()
                .id(analysis.getId())
                .usuarioId(analysis.getUsuarioId())
                .planId(analysis.getPlanId())
                .disabilityType(analysis.getDisabilityType())
                .puntajeGeneral(analysis.getPuntajeGeneral())
                .rangoMovimiento(analysis.getRangoMovimiento())
                .simetria(analysis.getSimetria())
                .estabilidad(analysis.getEstabilidad())
                .recomendaciones(analysis.getRecomendaciones())
                .timestamp(analysis.getFecha())
                .build();
    }
}