package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.FatigueRequest;
import com.inklusport.ai.dto.response.FatigueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FatigueDetectionService {

    public FatigueResponse evaluate(FatigueRequest request) {
        log.info("Analizando fatiga para usuario {}", request.getUserId());
        return FatigueResponse.builder()
                .status("stable")
                .fatigueScore(0.35)
                .message("Nivel de fatiga dentro de rango esperado")
                .build();
    }
}
