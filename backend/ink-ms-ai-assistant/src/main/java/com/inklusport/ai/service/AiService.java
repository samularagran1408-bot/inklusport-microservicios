package com.inklusport.ai.service;

import com.inklusport.ai.ai.GeminiLLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {

    private final GeminiLLMService geminiService;

    public String generateResponse(String prompt) {
        return geminiService.getAIResponse(prompt);
    }
}
