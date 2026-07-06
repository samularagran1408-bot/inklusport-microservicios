package com.inklusport.ai.service;

import com.inklusport.ai.model.DisabilityProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisabilityDetectionService {

    public DisabilityProfile detect(String userId) {
        log.info("Detectando perfil de discapacidad para usuario {}", userId);
        return DisabilityProfile.builder()
                .userId(userId)
                .needsSupport(false)
                .notes("Perfil base sin necesidades especiales detectadas")
                .build();
    }
}
