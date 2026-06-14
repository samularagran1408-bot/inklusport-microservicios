package com.inklusport.ai.service;

import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.model.ChatSession;
import com.inklusport.ai.model.ChatTraining;
import com.inklusport.ai.model.Mensaje;
import com.inklusport.ai.repository.ChatSessionRepository;
import com.inklusport.ai.repository.ChatTrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final ChatSessionRepository chatSessionRepository;
    private final ChatTrainingRepository chatTrainingRepository;

    public ChatResponse processMessage(String userId, ChatRequest request) {

        ChatSession session;

        /**
         * Buscar o crear sesión
         */
        if (request.getSessionId() != null) {
            session = chatSessionRepository.findById(request.getSessionId())
                    .orElseGet(() -> createNewSession(userId));
        } else {
            session = createNewSession(userId);
        }

        /**
         * Buscar respuesta en el entrenamiento del chatbot
         */
        String responseText = findResponse(request.getMessage());

        /**
         * Guardar Mensajes
         */
        saveUserMessage(session, request.getMessage());
        saveAssistantMessage(session, responseText);

        /**
         * Actualizar Sesión
         */
        session.setUltimaInteraccion(LocalDateTime.now());
        chatSessionRepository.save(session);

        log.info("Usuario {}: {}", userId, request.getMessage());
        log.info("Asistente: {}", responseText);

        return ChatResponse.builder()
                .sessionId(session.getId())
                .response(responseText)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    private ChatSession createNewSession(String userId) {
        ChatSession session = ChatSession.builder()
                .usuarioId(userId)
                .fechaInicio(LocalDateTime.now())
                .estado("activa")
                .ultimaInteraccion(LocalDateTime.now())
                .build();
        return chatSessionRepository.save(session);
    }

    private String findResponse(String message) {
        /**
         * Buscar por palabras clave
         */
        List<ChatTraining> trainings = chatTrainingRepository.findAll();

        for (ChatTraining training : trainings) {
            for (String keyword : training.getPalabrasClave()) {
                if (message.toLowerCase().contains(keyword.toLowerCase())) {
                    return traing.getRespuestaBase();
                }
            }
        }

        return "Lo siento, no entendí tu pregunta. ¿Puedes reformularla?";
    }

    private void saveUserMessage(ChatSession session, String message) {
        Mensaje mensaje = Mensaje.builder()
                .mensajeId(UUID.randomUUID().toString())
                .mensaje(message)
                .remitente("usuario")
                .fecha(LocalDateTime.now())
                .build();
        
        session.getMensajes().add(mensaje);
    }

    private void saveAssistantMessage(ChatSession session, String response) {
        Mensaje mensaje = Mensaje.builder()
                .mensajeId(UUID.randomUUID().toString())
                .mensaje(response)
                .remitente("asistente")
                .fecha(LocalDateTime.now())
                .build();

        session.getMensajes().add(mensaje)
    }

}
