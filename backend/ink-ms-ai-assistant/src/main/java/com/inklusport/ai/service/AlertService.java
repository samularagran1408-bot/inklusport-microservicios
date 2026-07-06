package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.AlertRequest;
import com.inklusport.ai.dto.response.AlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    public AlertResponse trigger(AlertRequest request) {
        log.info("Generando alerta para usuario {}", request.getUserId());
        return AlertResponse.builder()
                .message("Alerta generada")
                .severity("warning")
                .build();
    }
}
