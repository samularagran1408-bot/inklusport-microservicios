package com.inklusport.ai.service;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.model.ChatFeedback;
import com.inklusport.ai.repository.ChatFeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final ChatFeedbackRepository feedbackRepository;

    public FeedbackResponse saveFeedback(String userId, FeedbackRequest request) {
        log.info("Guardando feedback de usuario: {} para mensaje: {}", userId, request.getMensajeId());

        ChatFeedback feedback = ChatFeedback.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(userId)
                .mensajeId(request.getMensajeId())
                .util(request.getUtil())
                .comentario(request.getComentario())
                .fecha(LocalDateTime.now())
                .build();

        feedback = feedbackRepository.save(feedback);

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .mensajeId(feedback.getMensajeId())
                .util(feedback.getUtil())
                .comentario(feedback.getComentario())
                .fecha(feedback.getFecha())
                .build();
    }
}