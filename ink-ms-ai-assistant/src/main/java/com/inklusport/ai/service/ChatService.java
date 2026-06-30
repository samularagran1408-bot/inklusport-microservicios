package com.inklusport.ai.service;

import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.model.ChatSession;
import com.inklusport.ai.model.Mensaje;
import com.inklusport.ai.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final GeminiService geminiService;

    public ChatResponse processMessage(String userId, ChatRequest request) {
        String effectiveUserId = resolveUserId(userId, request.getUserId());
        log.info("📝 Procesando mensaje para usuario: {}", effectiveUserId);

        // 1. Obtener o crear sesión
        ChatSession session = getOrCreateSession(effectiveUserId, request.getSessionId());

        // 2. Guardar mensaje del usuario
        saveMessage(session, request.getMessage(), "usuario");

        // 3. Obtener respuesta de Gemini
        String response = geminiService.getAIResponse(
                request.getMessage(),
                session.getMensajes()
        );

        // 4. Guardar respuesta del asistente
        saveMessage(session, response, "asistente");

        // 5. Actualizar sesión
        session.setUltimaInteraccion(LocalDateTime.now());
        chatSessionRepository.save(session);

        return ChatResponse.builder()
                .sessionId(session.getId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ChatSession getOrCreateSession(String userId, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            var session = chatSessionRepository.findById(sessionId);
            if (session.isPresent()) {
                return session.get();
            }
        }
        return createNewSession(userId);
    }

    private ChatSession createNewSession(String userId) {
        ChatSession session = ChatSession.builder()
                .usuarioId(userId)
                .fechaInicio(LocalDateTime.now())
                .estado("activa")
                .ultimaInteraccion(LocalDateTime.now())
                .mensajes(new ArrayList<>())
                .build();
        return chatSessionRepository.save(session);
    }

    private void saveMessage(ChatSession session, String text, String remitente) {
        Mensaje mensaje = Mensaje.builder()
                .mensajeId(UUID.randomUUID().toString())
                .mensaje(text)
                .remitente(remitente)
                .fecha(LocalDateTime.now())
                .build();
        session.getMensajes().add(mensaje);
    }

    private String resolveUserId(String authUserId, String requestUserId) {
        if (authUserId != null && !authUserId.isEmpty() && !"anonymousUser".equals(authUserId)) {
            return authUserId;
        }
        return requestUserId;
    }
}