package com.inklusport.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final HuggingFaceService huggingFaceService;

    public String generateResponse(String prompt) {
        return huggingFaceService.getAIResponse(prompt);
    }
}
