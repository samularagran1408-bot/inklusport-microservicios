package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.VoiceCommandRequest;
import com.inklusport.ai.dto.response.VoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceAssistantService {

    public VoiceResponse processCommand(VoiceCommandRequest request) {
        log.info("Procesando comando por voz para usuario {}", request.getUserId());
        return VoiceResponse.builder()
                .commandType("assistant")
                .responseText("Comando recibido y preparado para ejecución")
                .build();
    }
}
