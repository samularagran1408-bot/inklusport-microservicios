package com.inklusport.ai.service;

import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.dto.ExerciseResponse;
import com.inklusport.ai.dto.TrainingPlanResponse;
import com.inklusport.ai.model.Ejercicio;
import com.inklusport.ai.model.TrainingPlan;
import com.inklusport.ai.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlanResponse generatePlan(String userId, TrainingPlanRequest request) {

        List<Ejercicio> ejercicios = generateExercises(request);

        TrainingPlan plan = TrainingPlan.builder()
                .usuarioId(userId)
                .nombre("Plan de " + request.getSport() + " - " + request.getDurationWeeks() + " semanas")
                .descripcion("Plan personalizado de " + request.getSport() + " adaptado para " + request.getDisabilityType())
                .disabilityType(request.getDisabilityType())
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusWeeks(request.getDurationWeeks()))
                .ejercicios(ejercicios)
                .objetivo("Mejorar rendimiento en " + request.getSport())
                .progresoPorcentaje(0.0)
                .activo(true)
                .build();
        
        plan = trainingPlanRepository.save(plan);
        log.info("Plan de entrenamiento generado para usuario: {}", userId);
        
        return convertToResponse(plan);
    }

    public List<TrainingPlanResponse> getMyPlans(String userId) {
        return trainingPlanRepository.findByUsuarioIdAndActivoTrue(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private List<Ejercicio> generateExercises(TrainingPlanRequest request) {
        List<Ejercicio> ejercicios = new ArrayList<>();

        if (request.getSport().toLowerCase().contains("natación")) {
            ejercicios.add(createEjercicio("Brazada adaptada", 10, 3, 30, "Usar flotadores"));
            ejercicios.add(createEjercicio("Patada con tabla", 15, 4, 25, "Tabla de flotación"));
            ejercicios.add(createEjercicio("Respiración lateral", 8, 3, 20, "Guía táctil"));
        } else if (request.getSport().toLowerCase().contains("goalball")) {
            ejercicios.add(createEjercicio("Lanzamiento", 20, 4, 30, "Balón sonoro"));
            ejercicios.add(createEjercicio("Bloqueo", 15, 3, 25, "Posición de escucha"));
        } else {
            ejercicios.add(createEjercicio("Ejercicio adaptado", 12, 3, 30, "Adaptaciones según necesidad"));
        }

        return ejercicios;
    }

    private Ejercicio createEjercicio(String nombre, int repeticiones, int series, int tiempo, String adaptaciones) {
        return Ejercicio.builder()
                .ejercicioId(UUID.randomUUID().toString())
                .nombre(nombre)
                .repeticiones(repeticiones)
                .series(series)
                .tiempoEstimado(tiempo)
                .build();
    }

    private TrainingPlanResponse convertToResponse(TrainingPlan plan) {
        List<ExerciseResponse> exercises = plan.getEjercicios().stream()
                .map(e -> ExerciseResponse.builder()
                        .name(e.getNombre())
                        .repetitions(e.getRepeticiones())
                        .sets(e.getSeries())
                        .estimatedTime(e.getTiempoEstimado())
                        .adaptations(null)
                        .build())
                .collect(Collectors.toList());
        
        return TrainingPlanResponse.builder()
                .planId(plan.getId())
                .name(plan.getNombre())
                .description(plan.getDescripcion())
                .exercises(exercises)
                .totalWeeks((int) (plan.getFechaFin().getDayOfYear() - plan.getFechaInicio().getDayOfYear()) / 7)
                .build();
    }
}
