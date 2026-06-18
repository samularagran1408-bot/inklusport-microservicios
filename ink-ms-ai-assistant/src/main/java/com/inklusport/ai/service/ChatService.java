package com.inklusport.ai.service;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.dto.ChatSessionResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.exception.CustomExceptions;
import com.inklusport.ai.model.ChatSession;
import com.inklusport.ai.model.ChatTraining;
import com.inklusport.ai.model.Mensaje;
import com.inklusport.ai.repository.ChatSessionRepository;
import com.inklusport.ai.repository.ChatTrainingRepository;
import com.inklusport.ai.util.TextProcessor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatTrainingRepository chatTrainingRepository;
    private final IntentClassifierService intentClassifierService;
    private final AdaptiveResponseService adaptiveResponseService;
    private final TextProcessor textProcessor;
    private final CacheService cacheService;
    private final MetricsService metricsService;

    private static final String DEFAULT_RESPONSE = "Lo siento, no entendí tu pregunta. ¿Puedes reformularla?";
    private static final int MAX_MESSAGES_PER_SESSION = 100;

    /**
     * Procesar mensaje del chat con resiliencia
     */
    @TimeLimiter(name = "chatService", fallbackMethod = "processMessageTimeoutFallback")
    @Retry(name = "chatService")
    @CircuitBreaker(name = "chatService", fallbackMethod = "processMessageCircuitFallback")
    @Transactional
    public CompletableFuture<ChatResponse> processMessage(String userId, ChatRequest request) {
        log.info("Procesando mensaje para usuario: {}", userId);
        
        try {
            /**
             * Validar mensaje
             */
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                throw new CustomExceptions.InvalidRequestException("El mensaje no puede estar vacío");
            }

            /**
             * Obtener o crear sesión
             */
            ChatSession session = getOrCreateSession(userId, request.getSessionId());
            
            /**
             * Actualizar tipo de discapacidad si se proporciona
             */
            if (request.getDisabilityType() != null && !request.getDisabilityType().isEmpty()) {
                session.setDisabilityType(request.getDisabilityType());
            }

            /**
             * Obtener respuesta
             */
            String response = findAdaptiveResponse(
                request.getMessage(), 
                session.getDisabilityType(),
                session
            );

            /**
             * Guardar mensajes
             */
            saveUserMessage(session, request.getMessage());
            saveAssistantMessage(session, response);

            /**
             * Actualizar sesión
             */
            session.setUltimaInteraccion(LocalDateTime.now());
            
            /**
             * Verificar límite de mensajes
             */
            if (session.getMensajes().size() > MAX_MESSAGES_PER_SESSION) {
                log.warn("Sesión {} excede el límite de mensajes", session.getId());
            }

            session = chatSessionRepository.save(session);

            /**
             * Registrar métricas
             */
            metricsService.recordChatInteraction(userId, session.getDisabilityType(), true);

            log.info("Mensaje procesado exitosamente para usuario: {}", userId);
            
            return CompletableFuture.completedFuture(buildChatResponse(session, response));

        } catch (Exception e) {
            log.error("Error procesando mensaje para usuario {}: {}", userId, e.getMessage(), e);
            metricsService.recordChatInteraction(userId, "unknown", false);
            throw e;
        }
    }

    /**
     * Fallback por Timeout
     */
    public CompletableFuture<ChatResponse> processMessageTimeoutFallback(
            String userId, ChatRequest request, Throwable ex) {
        log.warn("Timeout en chat para usuario: {}", userId);
        String response = "El servidor está tardando en responder. Por favor, intenta de nuevo en unos momentos.";
        return CompletableFuture.completedFuture(
            ChatResponse.builder()
                .sessionId(request.getSessionId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    /**
     * Fallback por Circuit Breaker 
     */
    public CompletableFuture<ChatResponse> processMessageCircuitFallback(
            String userId, ChatRequest request, Throwable ex) {
        log.warn("Circuit Breaker activado para usuario: {}", userId);
        String response = "El servicio de chat no está disponible temporalmente. Por favor, intenta más tarde.";
        return CompletableFuture.completedFuture(
            ChatResponse.builder()
                .sessionId(request.getSessionId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .metadata(Map.of(
                    "fallback", "circuit_breaker",
                    "error", ex.getMessage()
                ))
                .build()
        );
    }

    /**
     * Obtener o crear sesión
     */
    private ChatSession getOrCreateSession(String userId, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            Optional<ChatSession> existingSession = chatSessionRepository
                .findByIdAndUsuarioId(sessionId, userId);
            if (existingSession.isPresent()) {
                ChatSession session = existingSession.get();
                if (!"activa".equals(session.getEstado())) {
                    session.setEstado("activa");
                    session.setFechaInicio(LocalDateTime.now());
                }
                return session;
            }
        }
        return createNewSession(userId);
    }

    /**
     * Crear nueva sesión
     */
    private ChatSession createNewSession(String userId) {
        log.info("Creando nueva sesión para usuario: {}", userId);
        
        ChatSession session = ChatSession.builder()
                .usuarioId(userId)
                .disabilityType("no_especificado")
                .fechaInicio(LocalDateTime.now())
                .estado("activa")
                .ultimaInteraccion(LocalDateTime.now())
                .mensajes(new ArrayList<>())
                .build();
        
        return chatSessionRepository.save(session);
    }

    /**
     * Encontrar respuesta adaptada
     */
    @Cacheable(value = "chatResponses", key = "#message + '_' + #disabilityType")
    public String findAdaptiveResponse(String message, String disabilityType, ChatSession session) {
        log.info("Buscando respuesta para mensaje: '{}' - Tipo: {}", message, disabilityType);

        /**
         * 1. Clasificar intención
         */
        String intent = intentClassifierService.classifyIntent(message);
        log.debug("Intención detectada: {}", intent);

        /**
         * 2. Buscar por intención
         */
        List<ChatTraining> trainings = chatTrainingRepository.findByIntencionAndActivoTrue(intent);
        
        /**
         * 3. Si no hay, buscar por palabras clave
         */
        if (trainings.isEmpty()) {
            log.debug("No hay entrenamiento por intención, buscando por palabras clave");
            List<String> keywords = textProcessor.extractKeywords(message);
            trainings = chatTrainingRepository.findByPalabrasClaveIn(keywords);
        }

        /**
         * 4. Si sigue sin haber, usar todos los activos ordenados por prioridad
         */
        if (trainings.isEmpty()) {
            log.debug("Usando entrenamientos por prioridad");
            trainings = chatTrainingRepository.findByActivoTrueOrderByPrioridadDesc();
        }

        /**
         * 5. Buscar la mejor coincidencia
         */
        for (ChatTraining training : trainings) {
            if (training.getActivo() && matchesKeywords(message, training.getPalabrasClave())) {
                String response = adaptiveResponseService.getAdaptedResponse(training, disabilityType);
                log.info("Respuesta encontrada para intención: {}", training.getIntencion());
                
                /**
                 * Actualizar resumen de sesión
                 */
                updateSessionSummary(session, intent);
                
                return response;
            }
        }

        log.warn("No se encontró respuesta para: '{}'", message);
        return DEFAULT_RESPONSE;
    }

    /**
     * Verificar coincidencia de palabras clave
     */
    private boolean matchesKeywords(String message, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return false;
        }
        
        String messageLower = message.toLowerCase();
        return keywords.stream()
                .filter(Objects::nonNull)
                .anyMatch(keyword -> messageLower.contains(keyword.toLowerCase()));
    }

    /**
     * Guardar mensaje de usuario
     */
    private void saveUserMessage(ChatSession session, String message) {
        Mensaje mensaje = Mensaje.builder()
                .mensajeId(UUID.randomUUID().toString())
                .mensaje(message)
                .remitente("usuario")
                .fecha(LocalDateTime.now())
                .build();
        
        session.getMensajes().add(mensaje);
    }

    /**
     * Guardar mensaje de asistente
     */
    private void saveAssistantMessage(ChatSession session, String response) {
        Mensaje mensaje = Mensaje.builder()
                .mensajeId(UUID.randomUUID().toString())
                .mensaje(response)
                .remitente("asistente")
                .fecha(LocalDateTime.now())
                .build();
        
        session.getMensajes().add(mensaje);
    }

    /**
     * Actualizar resumen de sesión
     */
    private void updateSessionSummary(ChatSession session, String intent) {
        String currentSummary = session.getResumen();
        if (currentSummary == null) {
            currentSummary = "";
        }
        
        if (!currentSummary.contains(intent)) {
            session.setResumen(currentSummary + (currentSummary.isEmpty() ? "" : ", ") + intent);
        }
    }

    /**
     * Construir respuesta del chat
     */
    private ChatResponse buildChatResponse(ChatSession session, String response) {
        return ChatResponse.builder()
                .sessionId(session.getId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .metadata(Map.of(
                    "totalMessages", session.getMensajes().size(),
                    "disabilityType", session.getDisabilityType()
                ))
                .build();
    }

    /**
     * Obtener sesiones de un usuario con paginación
     */
    public PaginationResponse<ChatSessionResponse> getUserSessions(
            String userId, PaginationRequest pagination) {
        
        log.info("Obteniendo sesiones para usuario: {}", userId);
        
        PageRequest pageRequest = PageRequest.of(
            pagination.getPage(),
            pagination.getSize(),
            Sort.Direction.fromString(pagination.getSortDirection()),
            pagination.getSortBy()
        );
        
        Page<ChatSession> sessions = chatSessionRepository.findByUsuarioId(
            userId, pageRequest);
        
        List<ChatSessionResponse> content = sessions.getContent().stream()
                .map(this::toSessionResponse)
                .collect(Collectors.toList());
        
        return PaginationResponse.<ChatSessionResponse>builder()
                .content(content)
                .page(sessions.getNumber())
                .size(sessions.getSize())
                .totalElements(sessions.getTotalElements())
                .totalPages(sessions.getTotalPages())
                .first(sessions.isFirst())
                .last(sessions.isLast())
                .build();
    }

    /**
     * Obtener una sesión específica
     */
    @Cacheable(value = "chatSessions", key = "#sessionId + '_' + #userId")
    public ChatSessionResponse getSession(String sessionId, String userId) {
        log.info("Obteniendo sesión: {} para usuario: {}", sessionId, userId);
        
        ChatSession session = chatSessionRepository
                .findByIdAndUsuarioId(sessionId, userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                    "Sesión no encontrada para el usuario: " + userId));
        
        return toSessionResponse(session);
    }

    /**
     * Cerrar sesión
     */
    @Transactional
    @CacheEvict(value = "chatSessions", key = "#sessionId + '_' + #userId")
    public ChatSessionResponse closeSession(String sessionId, String userId) {
        log.info("Cerrando sesión: {} para usuario: {}", sessionId, userId);
        
        ChatSession session = chatSessionRepository
                .findByIdAndUsuarioId(sessionId, userId)
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException(
                    "Sesión no encontrada para el usuario: " + userId));
        
        session.setEstado("finalizada");
        session.setFechaFin(LocalDateTime.now());
        session = chatSessionRepository.save(session);
        
        return toSessionResponse(session);
    }

    /**
     * Convertir a ChatSessionResponse
     */
    private ChatSessionResponse toSessionResponse(ChatSession session) {
        List<Mensaje> mensajes = session.getMensajes() != null ? session.getMensajes() : Collections.emptyList();
        
        long mensajesUsuario = mensajes.stream()
                .filter(m -> "usuario".equals(m.getRemitente()))
                .count();
        long mensajesAsistente = mensajes.stream()
                .filter(m -> "asistente".equals(m.getRemitente()))
                .count();
        
        // Último mensaje
        String ultimoMensaje = null;
        String ultimoRemitente = null;
        LocalDateTime ultimoMensajeFecha = null;
        
        if (!mensajes.isEmpty()) {
            Mensaje last = mensajes.get(mensajes.size() - 1);
            ultimoMensaje = last.getMensaje();
            ultimoRemitente = last.getRemitente();
            ultimoMensajeFecha = last.getFecha();
        }
        
        long duracionMinutos = session.getFechaInicio() != null && session.getFechaFin() != null
                ? java.time.Duration.between(session.getFechaInicio(), session.getFechaFin()).toMinutes()
                : 0;

        return ChatSessionResponse.builder()
                .id(session.getId())
                .usuarioId(session.getUsuarioId())
                .disabilityType(session.getDisabilityType())
                .fechaInicio(session.getFechaInicio())
                .fechaFin(session.getFechaFin())
                .estado(session.getEstado())
                .mensajes(mensajes)
                .resumen(session.getResumen())
                .ultimaInteraccion(session.getUltimaInteraccion())
                .totalMensajes(mensajes.size())
                .mensajesUsuario((int) mensajesUsuario)
                .mensajesAsistente((int) mensajesAsistente)
                .duracionMinutos((double) duracionMinutos)
                .ultimoMensaje(ultimoMensaje)
                .ultimoRemitente(ultimoRemitente)
                .ultimoMensajeFecha(ultimoMensajeFecha)
                .createdAt(session.getCreatedAt())
                .build();
    }

    /**
     * Obtener estadísticas del chat
     */
    public Map<String, Object> getChatStats(String userId) {
        log.info("Obteniendo estadísticas de chat para usuario: {}", userId);
        
        List<ChatSession> sessions = chatSessionRepository.findByUsuarioId(userId);
        
        if (sessions.isEmpty()) {
            return Map.of(
                "totalSessions", 0,
                "totalMessages", 0,
                "avgMessagesPerSession", 0.0
            );
        }
        
        int totalSessions = sessions.size();
        int totalMessages = sessions.stream()
                .mapToInt(s -> s.getMensajes() != null ? s.getMensajes().size() : 0)
                .sum();
        double avgMessages = (double) totalMessages / totalSessions;
        
        return Map.of(
            "totalSessions", totalSessions,
            "totalMessages", totalMessages,
            "avgMessagesPerSession", avgMessages,
            "lastSession", sessions.stream()
                .max(Comparator.comparing(ChatSession::getUltimaInteraccion))
                .map(s -> s.getUltimaInteraccion().toString())
                .orElse(null)
        );
    }
}