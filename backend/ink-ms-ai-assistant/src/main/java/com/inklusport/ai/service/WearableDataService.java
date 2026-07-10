package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.WearableDataRequest;
import com.inklusport.ai.dto.response.AlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WearableDataService {

    private final HuggingFaceService huggingFaceService;

    public AlertResponse ingest(WearableDataRequest request) {
        log.info("Recibiendo datos de wearable para usuario {}", request.getUserId());

        String analysis = huggingFaceService.generate(
                "Eres un analista de datos de wearables para deportes adaptados.",
                String.format("Interpreta datos de wearable del usuario %s. Frecuencia cardíaca: %.0f bpm. Pasos: %.0f. " +
                                "Genera insight accionable en español.",
                        request.getUserId(),
                        request.getHeartRate(),
                        request.getSteps()));

        return AlertResponse.builder()
                .message(analysis)
                .severity("info")
                .build();
    }
}
