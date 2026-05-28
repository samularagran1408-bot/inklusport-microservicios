package com.inklusport.ia.service;

import com.inklusport.ia.dto.request.PlanEntrenamientoRequest;
import com.inklusport.ia.dto.response.PlanEntrenamientoResponse;

public interface PlanEntrenamientoService {
    PlanEntrenamientoResponse crearOActualizarPlan(PlanEntrenamientoRequest request);
}
