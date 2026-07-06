package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.CompetitionRequest;
import com.inklusport.ai.dto.response.CompetitionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompetitionService {

    public CompetitionResponse prepare(CompetitionRequest request) {
        log.info("Preparando modo competencia para usuario {}", request.getUserId());
        return CompetitionResponse.builder()
                .mode("competition")
                .status("ready")
                .build();
    }
}
