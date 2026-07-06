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

    public AlertResponse ingest(WearableDataRequest request) {
        log.info("Recibiendo datos de wearable para usuario {}", request.getUserId());
        return AlertResponse.builder()
                .message("Datos de wearable registrados")
                .severity("info")
                .build();
    }
}
