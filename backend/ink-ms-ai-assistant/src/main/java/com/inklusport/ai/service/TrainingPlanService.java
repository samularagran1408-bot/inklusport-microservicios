package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.TrainingPlanRequest;
import com.inklusport.ai.dto.response.TrainingPlanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingPlanService {

    public TrainingPlanResponse buildPlan(TrainingPlanRequest request) {
        log.info("Solicitando plan de entrenamiento para usuario {}", request.getUserId());
        return TrainingPlanResponse.builder()
                .planName("Plan personalizado inicial")
                .focusAreas(java.util.List.of("resistencia", "movilidad"))
                .estimatedDurationMinutes(45)
                .build();
    }
}
