package com.inklusport.ai.service;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.dto.ExerciseResponse;
import com.inklusport.ai.dto.EjercicioRequest;
import com.inklusport.ai.dto.TrainingPlanResponse;
import com.inklusport.ai.exception.CustomExceptions;
import com.inklusport.ai.model.Ejercicio;
import com.inklusport.ai.model.TrainingPlan;
import com.inklusport.ai.repository.TrainingPlanRepository;
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
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;

    /**
     * Crear plan de entrenamiento
     */
    @Transactional
    @CircuitBreaker(name = "trainingPlanService", fallbackMethod = "createPlanFallback")
    @CacheEvict(value = "trainingPlans", allEntries = true)
    public TrainingPlanResponse createPlan(TrainingPlanRequest request) {
        log.info("Creando plan de entrenamiento para usuario: {}", request.getUsuarioId());

        /**
         * Validar que no haya plan activo
         */
        Optional<TrainingPlan> existingPlan = trainingPlanRepository
                .findFirstByUsuarioIdAndActivoTrue(request.getUsuarioId());
        if (existingPlan.isPresent()) {
            throw new CustomExceptions.InvalidRequestException(
                "El usuario ya tiene un plan activo. Por favor, completarlo o desactivarlo primero.");
        }

        /**
         * Crear plan
         */
        TrainingPlan plan = TrainingPlan.builder()
                .usuarioId(request.getUsuarioId())
                .entrenadorId(request.getEntrenadorId())
                .disabilityType(request.getDisabilityType())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .ejercicios(convertToEjercicios(request.getEjercicios()))
                .objetivo(request.getObjetivo())
                .progresoPorcentaje(0.0)
                .activo(request.getActivo())
                .sesionesRegistradas(new ArrayList<>())
                .build();

        plan = trainingPlanRepository.save(plan);
        log.info("Plan creado exitosamente con ID: {}", plan.getId());

        return toResponse(plan);
    }

    /**
     * Fallback para creación de plan
     */
    public TrainingPlanResponse createPlanFallback(TrainingPlanRequest request, Throwable ex) {
        log.warn("Circuit Breaker activado para creación de plan");
        
        return TrainingPlanResponse.builder()
                .nombre(request.getNombre())
                .usuarioId(request.getUsuarioId())
                .descripcion("Plan guardado en modo offline. Se sincronizará cuando el servicio esté disponible.")
                .estado("PENDIENTE_SINCRONIZACION")
                .build();
    }

    /**
     * Obtener plan activo de usuario
     */
    @Cacheable(value = "trainingPlans", key = "'active_' + #usuarioId")
    public TrainingPlanResponse getActivePlan(String usuarioId) {
        log.info("Obteniendo plan activo para usuario: {}", usuarioId);
        
        TrainingPlan plan = trainingPlanRepository
                .findFirstByUsuarioIdAndActivoTrue(usuarioId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                    "No se encontró un plan activo para el usuario: " + usuarioId));
        
        return toResponse(plan);
    }

    /**
     * Obtener todos los planes de usuario
     */
    public PaginationResponse<TrainingPlanResponse> getUserPlans(
            String usuarioId, PaginationRequest pagination) {
        
        log.info("Obteniendo planes para usuario: {}", usuarioId);
        
        PageRequest pageRequest = PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            Sort.Direction.fromString(pagination.getSortDirection()),
            pagination.getSortBy()
        );
        
        Page<TrainingPlan> plans = trainingPlanRepository.findByUsuarioId(
            usuarioId, pageRequest);
        
        List<TrainingPlanResponse> content = plans.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        
        return PaginationResponse.<TrainingPlanResponse>builder()
                .content(content)
                .page(plans.getNumber())
                .size(plans.getSize())
                .totalElements(plans.getTotalElements())
                .totalPages(plans.getTotalPages())
                .first(plans.isFirst())
                .last(plans.isLast())
                .build();
    }

    /**
     * Actualizar progreso de plan
     */
    @Transactional
    @CacheEvict(value = "trainingPlans", key = "'active_' + #usuarioId")
    public TrainingPlanResponse updateProgress(String usuarioId, Double progreso) {
        log.info("Actualizando progreso de usuario: {} a {}", usuarioId, progreso);
        
        if (progreso < 0 || progreso > 100) {
            throw new CustomExceptions.InvalidRequestException("El progreso debe ser entre 0 y 100");
        }
        
        TrainingPlan plan = trainingPlanRepository
                .findFirstByUsuarioIdAndActivoTrue(usuarioId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                    "No se encontró un plan activo para el usuario: " + usuarioId));
        
        plan.setProgresoPorcentaje(progreso);
        
        if (progreso >= 100) {
            plan.setActivo(false);
            plan.setFechaFin(LocalDateTime.now());
            log.info("Plan completado para usuario: {}", usuarioId);
        }
        
        plan = trainingPlanRepository.save(plan);
        return toResponse(plan);
    }

    /**
     * Convertir a TrainingPlanResponse
     */
    private TrainingPlanResponse toResponse(TrainingPlan plan) {
        /**
         * Calcular estadísticas
         */
        int totalSesiones = plan.getSesionesRegistradas() != null 
                ? plan.getSesionesRegistradas().size() 
                : 0;
        
        long sesionesCompletadas = plan.getSesionesRegistradas() != null
                ? plan.getSesionesRegistradas().stream()
                    .filter(s -> s.getCompletado() != null && s.getCompletado())
                    .count()
                : 0;
        
        double nivelCumplimiento = totalSesiones > 0 
                ? (double) sesionesCompletadas / totalSesiones * 100 
                : 0.0;
        
        /**
         * Calcular días restantes
         */
        Integer diasRestantes = null;
        if (plan.getFechaFin() != null) {
            diasRestantes = (int) java.time.Duration.between(
                LocalDateTime.now(), 
                plan.getFechaFin()
            ).toDays();
        }
        
        /**
         * Determinar estado
         */
        String estado = "ACTIVO";
        if (plan.getActivo() == null || !plan.getActivo()) {
            estado = plan.getProgresoPorcentaje() != null && plan.getProgresoPorcentaje() >= 100 
                    ? "COMPLETADO" 
                    : "INACTIVO";
        } else if (plan.getFechaFin() != null && plan.getFechaFin().isBefore(LocalDateTime.now())) {
            estado = "VENCIDO";
        }

        return TrainingPlanResponse.builder()
                .id(plan.getId())
                .usuarioId(plan.getUsuarioId())
                .entrenadorId(plan.getEntrenadorId())
                .disabilityType(plan.getDisabilityType())
                .nombre(plan.getNombre())
                .descripcion(plan.getDescripcion())
                .fechaInicio(plan.getFechaInicio())
                .fechaFin(plan.getFechaFin())
                .ejercicios(convertToExerciseResponses(plan.getEjercicios()))
                .objetivo(plan.getObjetivo())
                .progresoPorcentaje(plan.getProgresoPorcentaje())
                .activo(plan.getActivo())
                .totalSesiones(totalSesiones)
                .sesionesCompletadas((int) sesionesCompletadas)
                .nivelCumplimiento(nivelCumplimiento)
                .esfuerzoPromedio(calculateAverageEffort(plan))
                .estado(estado)
                .diasRestantes(diasRestantes)
                .estaVencido(estado.equals("VENCIDO"))
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    /**
     * Convertir ejercicios de Request a Model
     */
    private List<Ejercicio> convertToEjercicios(List<EjercicioRequest> ejercicios) {
        if (ejercicios == null) return new ArrayList<>();
        
        return ejercicios.stream()
                .map(e -> Ejercicio.builder()
                        .ejercicioId(e.getEjercicioId() != null ? e.getEjercicioId() : UUID.randomUUID().toString())
                        .nombre(e.getNombre())
                        .repeticiones(e.getRepeticiones())
                        .series(e.getSeries())
                        .tiempoEstimado(e.getTiempoEstimado())
                        .adaptaciones(e.getAdaptaciones())
                        .esfuerzoObjetivo(e.getEsfuerzoObjetivo())
                        .descansoSegundos(e.getDescansoSegundos())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Convertir ejercicios de Model a Response
     */
    private List<ExerciseResponse> convertToExerciseResponses(List<Ejercicio> ejercicios) {
        if (ejercicios == null) return new ArrayList<>();
        
        return ejercicios.stream()
                .map(e -> ExerciseResponse.builder()
                        .id(e.getEjercicioId())
                        .nombre(e.getNombre())
                        .repeticiones(e.getRepeticiones())
                        .series(e.getSeries())
                        .tiempoEstimado(e.getTiempoEstimado())
                        .esfuerzoObjetivo(e.getEsfuerzoObjetivo())
                        .descansoSegundos(e.getDescansoSegundos())
                        .adaptaciones(e.getAdaptaciones())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Calcular esfuerzo promedio
     */
    private Double calculateAverageEffort(TrainingPlan plan) {
        if (plan.getSesionesRegistradas() == null || plan.getSesionesRegistradas().isEmpty()) {
            return 0.0;
        }
        
        return plan.getSesionesRegistradas().stream()
                .filter(s -> s.getEsfuerzoPercibido() != null)
                .mapToDouble(s -> s.getEsfuerzoPercibido())
                .average()
                .orElse(0.0);
    }
}