package com.inklusport.ai.service;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.dto.FeedbackStats;
import com.inklusport.ai.model.ChatFeedback;
import com.inklusport.ai.repository.ChatFeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final ChatFeedbackRepository feedbackRepository;

    /**
     * Guardar feedback
     */
    @Transactional
    @CacheEvict(value = "feedbackStats", allEntries = true)
    public FeedbackResponse saveFeedback(FeedbackRequest request) {
        log.info("Guardando feedback para mensaje: {}", request.getMensajeId());
        
        /**
         * Verificar que no exista feedback previo para este mensaje
         */
        Optional<ChatFeedback> existing = feedbackRepository
                .findByMensajeId(request.getMensajeId());
        
        if (existing.isPresent()) {
            /**
             * Actualizar feedback existente
             */
            ChatFeedback feedback = existing.get();
            feedback.setUtil(request.getUtil());
            feedback.setComentario(request.getComentario());
            feedback.setFecha(LocalDateTime.now());
            feedback = feedbackRepository.save(feedback);
            return toResponse(feedback);
        }
        
        /**
         * Crear nuevo feedback
         */
        ChatFeedback feedback = ChatFeedback.builder()
                .conversacionId(request.getConversacionId())
                .usuarioId(request.getUsuarioId())
                .mensajeId(request.getMensajeId())
                .util(request.getUtil())
                .comentario(request.getComentario())
                .fecha(LocalDateTime.now())
                .build();
        
        feedback = feedbackRepository.save(feedback);
        log.info("Feedback guardado exitosamente para mensaje: {}", request.getMensajeId());
        
        return toResponse(feedback);
    }

    /**
     * Obtener feedback de una conversación
     */
    @Cacheable(value = "feedback", key = "#conversacionId")
    public List<FeedbackResponse> getFeedbackByConversation(String conversacionId) {
        log.info("Obteniendo feedback para conversación: {}", conversacionId);
        
        List<ChatFeedback> feedbacks = feedbackRepository
                .findByConversacionId(conversacionId);
        
        return feedbacks.stream()
                .map((ChatFeedback feedback) -> toResponse(feedback))
                .collect(Collectors.toList());
    }

    /**
     * Obtener estadísticas de feedback
     */
    @Cacheable(value = "feedbackStats", key = "#usuarioId != null ? #usuarioId : 'global'")
    public FeedbackStats getFeedbackStats(String usuarioId) {
        log.info("Obteniendo estadísticas de feedback para usuario: {}", usuarioId);
        
        List<ChatFeedback> feedbacks;
        if (usuarioId != null && !usuarioId.isEmpty()) {
            feedbacks = feedbackRepository.findByUsuarioId(usuarioId);
        } else {
            feedbacks = feedbackRepository.findAll();
        }
        
        if (feedbacks.isEmpty()) {
            return buildEmptyStats();
        }
        
        /**
         * Estadísticas básicas
         */
        long total = feedbacks.size();
        long useful = feedbacks.stream().filter(ChatFeedback::getUtil).count();
        long notUseful = total - useful;
        double usefulPercentage = total > 0 ? (double) useful / total * 100 : 0.0;
        
        /**
         * Feedback por día
         */
        Map<String, Long> feedbackByDay = feedbacks.stream()
                .collect(Collectors.groupingBy(
                    f -> f.getFecha().truncatedTo(ChronoUnit.DAYS).toString(),
                    Collectors.counting()
                ));
        
        /**
         * Feedback por hora
         */
        Map<String, Long> feedbackByHour = feedbacks.stream()
                .collect(Collectors.groupingBy(
                    f -> String.valueOf(f.getFecha().getHour()),
                    Collectors.counting()
                ));
        
        /**
         * Top usuarios por feedback útil
         */
        Map<String, Long> topUsersByUseful = feedbacks.stream()
                .filter(ChatFeedback::getUtil)
                .collect(Collectors.groupingBy(
                    ChatFeedback::getUsuarioId,
                    Collectors.counting()
                ));
        
        /**
         * Calcular satisfacción (métrica simple basada en feedback útil)
         */
        double satisfactionScore = usefulPercentage / 100;
        
        return FeedbackStats.builder()
                .totalFeedback(total)
                .usefulCount(useful)
                .notUsefulCount(notUseful)
                .usefulPercentage(usefulPercentage)
                .feedbackByDay(feedbackByDay)
                .feedbackByHour(feedbackByHour)
                .topUsersByUseful(topUsersByUseful)
                .averageRating(usefulPercentage / 20) /** Conversión a escala 1-5 */
                .satisfactionScore(satisfactionScore)
                .lastUpdated(LocalDateTime.now())
                .period(usuarioId != null ? "USER" : "GLOBAL")
                .build();
    }

    /**
     * Construir estadísticas vacías
     */
    private FeedbackStats buildEmptyStats() {
        return FeedbackStats.builder()
                .totalFeedback(0L)
                .usefulCount(0L)
                .notUsefulCount(0L)
                .usefulPercentage(0.0)
                .averageRating(0.0)
                .satisfactionScore(0.0)
                .lastUpdated(LocalDateTime.now())
                .period("EMPTY")
                .build();
    }

    /**
     * Convertir a FeedbackResponse
     */
    private FeedbackResponse toResponse(ChatFeedback feedback) {
        String feedbackType = "NEUTRO";
        if (feedback.getUtil() != null) {
            feedbackType = feedback.getUtil() ? "POSITIVO" : "NEGATIVO";
        }
        
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .conversacionId(feedback.getConversacionId())
                .usuarioId(feedback.getUsuarioId())
                .mensajeId(feedback.getMensajeId())
                .util(feedback.getUtil())
                .comentario(feedback.getComentario())
                .fecha(feedback.getFecha())
                .feedbackType(feedbackType)
                .build();
    }
}