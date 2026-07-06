package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.InjuryPredictionRequest;
import com.inklusport.ai.dto.response.InjuryPredictionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InjuryPredictionService {

    public InjuryPredictionResponse predict(InjuryPredictionRequest request) {
        log.info("Evaluando riesgo de lesión para usuario {}", request.getUserId());
        return InjuryPredictionResponse.builder()
                .riskLevel("low")
                .confidence(0.72)
                .recommendation("Mantener progresión gradual")
                .build();
    }
}
