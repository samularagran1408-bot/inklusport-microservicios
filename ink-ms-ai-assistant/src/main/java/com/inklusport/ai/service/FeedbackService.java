package com.inklusport.ai.service;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.model.ChatFeedback;
import com.inklusport.ai.repository.ChatFeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final ChatFeedbackRepository feedbackRepository;

    /**
     * Registrar feedback de un usuario sobre una respuesta del chatbot
     */
    public FeedbackResponse registrarFeedback(String userId, FeedbackRequest request) {
        ChatFeedback feedback = ChatFeedback.builder()
                .conversacionId(request.getConversacionId())
                .usuarioId(userId)
                .mensajeId(request.getMensajeId())
                .util(request.getUtil())
                .comentario(request.getComentario())
                .fecha(LocalDateTime.now())
                .build();

        feedback = feedbackRepository.save(feedback);
        log.info("Feedback registrado - Usuario: {}, Útil: {}", userId, request.getUtil());

        return convertToResponse(feedback);
    }

    /**
     * Obtener todos los feedbacks de un usuario
     */
    public List<FeedbackResponse> getFeedbacksByUser(String userId) {
        return feedbackRepository.findByUsuarioId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener feedbacks de una conversación específica
     */
    public List<FeedbackResponse> getFeedbacksByConversacion(String conversacionId) {
        return feedbackRepository.findByConversacionId(conversacionId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener estadísticas de feedback (qué tan útil es el chatbot)
     */
    public FeedbackStats getFeedbackStats() {
        List<ChatFeedback> allFeedbacks = feedbackRepository.findAll();
        
        long total = allFeedbacks.size();
        long utilCount = allFeedbacks.stream().filter(ChatFeedback::getUtil).count();
        
        double porcentajeUtil = total > 0 ? (utilCount * 100.0 / total) : 0.0;
        
        return FeedbackStats.builder()
                .totalFeedbacks(total)
                .utilCount(utilCount)
                .porcentajeUtil(Math.round(porcentajeUtil * 10) / 10.0)
                .build();
    }

    private FeedbackResponse convertToResponse(ChatFeedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .conversacionId(feedback.getConversacionId())
                .usuarioId(feedback.getUsuarioId())
                .mensajeId(feedback.getMensajeId())
                .util(feedback.getUtil())
                .comentario(feedback.getComentario())
                .fecha(feedback.getFecha())
                .build();
    }
}