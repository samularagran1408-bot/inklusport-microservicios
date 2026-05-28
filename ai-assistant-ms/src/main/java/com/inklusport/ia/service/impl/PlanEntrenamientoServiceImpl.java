package com.inklusport.ia.service.impl;

import com.inklusport.ia.document.PlanEntrenamientoDocument;
import com.inklusport.ia.dto.request.PlanEntrenamientoRequest;
import com.inklusport.ia.dto.response.PlanEntrenamientoResponse;
import com.inklusport.ia.repository.PlanEntrenamientoRepository;
import com.inklusport.ia.service.PlanEntrenamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanEntrenamientoServiceImpl implements PlanEntrenamientoService {

    private final PlanEntrenamientoRepository planEntrenamientoRepository;

    @Override
    public PlanEntrenamientoResponse crearOActualizarPlan(PlanEntrenamientoRequest request) {
        Optional<PlanEntrenamientoDocument> existente = planEntrenamientoRepository.findAll().stream()
                .filter(plan -> request.getUsuarioId().equals(plan.getUsuarioId()))
                .filter(plan -> request.getEntrenadorId().equals(plan.getEntrenadorId()))
                .findFirst();

        PlanEntrenamientoDocument plan = existente.orElseGet(PlanEntrenamientoDocument::new);
        plan.setUsuarioId(request.getUsuarioId());
        plan.setEntrenadorId(request.getEntrenadorId());
        plan.setEjercicios(mapEjerciciosRequestToDocument(request.getEjercicios()));
        plan.setUpdatedAt(Instant.now());

        PlanEntrenamientoDocument saved = planEntrenamientoRepository.save(plan);
        return toResponse(saved);
    }

    private List<PlanEntrenamientoDocument.EjercicioAdaptado> mapEjerciciosRequestToDocument(
            List<PlanEntrenamientoRequest.EjercicioAdaptadoRequest> ejerciciosRequest
    ) {
        return ejerciciosRequest.stream()
                .map(item -> {
                    PlanEntrenamientoDocument.EjercicioAdaptado ejercicio = new PlanEntrenamientoDocument.EjercicioAdaptado();
                    ejercicio.setNombreEjercicio(item.getNombreEjercicio());
                    ejercicio.setAdaptaciones(item.getAdaptaciones());
                    return ejercicio;
                })
                .collect(Collectors.toList());
    }

    private PlanEntrenamientoResponse toResponse(PlanEntrenamientoDocument document) {
        return PlanEntrenamientoResponse.builder()
                .id(document.getId())
                .usuarioId(document.getUsuarioId())
                .entrenadorId(document.getEntrenadorId())
                .ejercicios(document.getEjercicios().stream()
                        .map(ejercicio -> PlanEntrenamientoResponse.EjercicioAdaptadoResponse.builder()
                                .nombreEjercicio(ejercicio.getNombreEjercicio())
                                .adaptaciones(ejercicio.getAdaptaciones())
                                .build())
                        .collect(Collectors.toList()))
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
