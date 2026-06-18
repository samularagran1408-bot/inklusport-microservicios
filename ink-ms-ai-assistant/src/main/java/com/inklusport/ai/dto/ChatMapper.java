package com.inklusport.ai.dto;

import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.model.ChatSession;
import com.inklusport.ai.model.ChatTraining;
import com.inklusport.ai.model.Mensaje;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChatMapper {

    public ChatResponse toResponse(ChatTraining training, String sessionId, String disabilityType) {
        return ChatResponse.builder()
                .sessionId(sessionId)
                .response(training.getRespuestaBase())
                .intencion(training.getIntencion())
                .disabilityType(disabilityType)
                .isAdapted(training.getRespuestaAdaptada() != null && 
                          training.getRespuestaAdaptada().containsKey(disabilityType))
                .timestamp(LocalDateTime.now())
                .metadata(Map.of(
                    "prioridad", training.getPrioridad(),
                    "activo", training.getActivo()
                ))
                .build();
    }

    public ChatSession toEntity(ChatRequest request, String userId) {
        return ChatSession.builder()
                .usuarioId(userId)
                .disabilityType(request.getDisabilityType())
                .fechaInicio(LocalDateTime.now())
                .estado("activa")
                .ultimaInteraccion(LocalDateTime.now())
                .mensajes(new ArrayList<>())
                .build();
    }
}