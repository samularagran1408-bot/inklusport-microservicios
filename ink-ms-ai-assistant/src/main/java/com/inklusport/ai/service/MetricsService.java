package com.inklusport.ai.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class MetricsService {

    private final MeterRegistry meterRegistry;
    
    /**
     * Contadores
     */
    private final Counter chatMessagesTotal;
    private final Counter chatMessagesSuccess;
    private final Counter chatMessagesError;
    private final Counter chatSessionsCreated;
    
    /**
     * Timers
     */
    private final Timer chatProcessingTime;
    private final Timer biomechanicalProcessingTime;
    private final Timer trainingPlanProcessingTime;
    
    /**
     * Métricas en memoria
     */
    private final Map<String, AtomicLong> userMessagesCount = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> userSessionsCount = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        /**
         * Inicializar contadores
         */
        this.chatMessagesTotal = Counter.builder("chat.messages.total")
                .description("Total de mensajes de chat procesados")
                .register(meterRegistry);
        
        this.chatMessagesSuccess = Counter.builder("chat.messages.success")
                .description("Mensajes de chat procesados exitosamente")
                .register(meterRegistry);
        
        this.chatMessagesError = Counter.builder("chat.messages.error")
                .description("Mensajes de chat con error")
                .register(meterRegistry);
        
        this.chatSessionsCreated = Counter.builder("chat.sessions.created")
                .description("Sesiones de chat creadas")
                .register(meterRegistry);
        
        /**
         * Inicializar timers
         */
        this.chatProcessingTime = Timer.builder("chat.processing.time")
                .description("Tiempo de procesamiento de mensajes de chat")
                .register(meterRegistry);
        
        this.biomechanicalProcessingTime = Timer.builder("biomechanical.processing.time")
                .description("Tiempo de procesamiento de análisis biomecánico")
                .register(meterRegistry);
        
        this.trainingPlanProcessingTime = Timer.builder("training.plan.processing.time")
                .description("Tiempo de procesamiento de planes de entrenamiento")
                .register(meterRegistry);
    }

    /**
     * Registrar interacción de chat
     */
    public void recordChatInteraction(String userId, String disabilityType, boolean success) {
        chatMessagesTotal.increment();
        
        if (success) {
            chatMessagesSuccess.increment();
        } else {
            chatMessagesError.increment();
        }
        
        /**
         * Registrar por usuario
         */
        userMessagesCount.computeIfAbsent(userId, k -> new AtomicLong()).incrementAndGet();
        userMessagesCount.computeIfAbsent(disabilityType, k -> new AtomicLong()).incrementAndGet();
    }

    /**
     * Registrar creación de sesión
     */
    public void recordSessionCreated(String userId) {
        chatSessionsCreated.increment();
        userSessionsCount.computeIfAbsent(userId, k -> new AtomicLong()).incrementAndGet();
    }

    /**
     * Medir tiempo de procesamiento de chat
     */
    public Timer.Sample startChatTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Detener timer de chat
     */
    public void stopChatTimer(Timer.Sample sample) {
        sample.stop(chatProcessingTime);
    }

    /**
     * Obtener estadísticas de usuario
     */
    public Map<String, Long> getUserStats(String userId) {
        return Map.of(
            "messages", userMessagesCount.getOrDefault(userId, new AtomicLong(0)).get(),
            "sessions", userSessionsCount.getOrDefault(userId, new AtomicLong(0)).get()
        );
    }

    /**
     * Obtener métricas globales
     */
    public Map<String, Object> getGlobalMetrics() {
        return Map.of(
            "totalMessages", chatMessagesTotal.count(),
            "successMessages", chatMessagesSuccess.count(),
            "errorMessages", chatMessagesError.count(),
            "successRate", calculateSuccessRate(),
            "totalSessions", chatSessionsCreated.count(),
            "activeUsers", userMessagesCount.size()
        );
    }

    /**
     * Calcular tasa de éxito
     */
    private double calculateSuccessRate() {
        double total = chatMessagesTotal.count();
        if (total == 0) return 100.0;
        return (chatMessagesSuccess.count() / total) * 100;
    }
}