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
    private final HuggingFaceService huggingFaceService;

    public ChatResponse processMessage(String userId, ChatRequest request) {
        log.info(" Procesando mensaje para usuario: {}", userId);

        /**
         * 1. Obtener o crear sesión
         */
        ChatSession session = getOrCreateSession(userId, request.getSessionId());

        /**
         * 2. Guardar mensaje del usuario
         */
        saveMessage(session, request.getMessage(), "usuario");

        /**
         * 3. Obtener respuesta de Hugging Face (con historial de la sesión)
         */
        String history = buildConversationHistory(session);
        String response = huggingFaceService.getAIResponseWithHistory(request.getMessage(), history);

        /** 4. Guardar respuesta del asistente */
        saveMessage(session, response, "asistente");

        /**
         * 5. Actualizar sesión
         */
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

    private String buildConversationHistory(ChatSession session) {
        if (session.getMensajes() == null || session.getMensajes().size() <= 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        var messages = session.getMensajes();
        int start = Math.max(0, messages.size() - 11);
        for (int i = start; i < messages.size() - 1; i++) {
            Mensaje m = messages.get(i);
            sb.append(m.getRemitente()).append(": ").append(m.getMensaje()).append("\n");
        }
        return sb.toString();
    }

    public java.util.List<ChatSession> getUserSessions(String userId) {
        return chatSessionRepository.findByUsuarioIdOrderByUltimaInteraccionDesc(userId);
    }

    public ChatSession getSession(String sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new com.inklusport.ai.exception.CustomExceptions.ResourceNotFoundException(
                        "Sesión no encontrada: " + sessionId));
    }

    public void closeSession(String sessionId) {
        ChatSession session = getSession(sessionId);
        session.setEstado("cerrada");
        session.setUltimaInteraccion(LocalDateTime.now());
        chatSessionRepository.save(session);
    }
}