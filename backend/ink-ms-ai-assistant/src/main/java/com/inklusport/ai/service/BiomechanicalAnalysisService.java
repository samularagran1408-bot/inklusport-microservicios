package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.BiomechanicalRequest;
import com.inklusport.ai.dto.response.BiomechanicalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiomechanicalAnalysisService {

    public BiomechanicalResponse analyze(BiomechanicalRequest request) {
        log.info("Recibido análisis biomecánico para usuario {}", request.getUserId());
        return BiomechanicalResponse.builder()
                .summary("Análisis biomecánico inicial preparado")
                .riskLevel("medium")
                .recommendation("Revisar técnica y monitorear carga")
                .build();
    }
}
