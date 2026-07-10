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

    private final HuggingFaceService huggingFaceService;

    public VoiceResponse processCommand(VoiceCommandRequest request) {
        log.info("Procesando comando por voz para usuario {}", request.getUserId());

        String prompt = String.format("""
                Comando de voz del usuario %s: %s

                Responde de forma breve, clara y fácil de leer en voz alta.
                Usa frases cortas y evita jerga técnica.
                """,
                request.getUserId(),
                request.getTranscript() != null ? request.getTranscript() : "Consulta general");

        String response = huggingFaceService.generate(
                "Eres un asistente de voz adaptado para personas con discapacidad en deportes inclusivos.",
                prompt);

        return VoiceResponse.builder()
                .commandType("assistant")
                .responseText(response)
                .build();
    }
}
