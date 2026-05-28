package com.inklusport.ia.service.impl;

import com.inklusport.ia.config.IaProperties;
import com.inklusport.ia.document.ConversacionChatbotDocument;
import com.inklusport.ia.dto.request.ChatbotQueryRequest;
import com.inklusport.ia.dto.response.ChatbotQueryResponse;
import com.inklusport.ia.repository.ConversacionChatbotRepository;
import com.inklusport.ia.service.ChatbotRespuestaService;
import com.inklusport.ia.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_CERRADA = "CERRADA";

    private final ConversacionChatbotRepository conversacionChatbotRepository;
    private final ChatbotRespuestaService chatbotRespuestaService;
    private final IaProperties iaProperties;

    @Override
    public ChatbotQueryResponse procesarMensaje(ChatbotQueryRequest request) {
        Optional<ConversacionChatbotDocument> activa = conversacionChatbotRepository
                .findByUsuarioIdAndEstadoConversacionIgnoreCase(request.getUsuarioId(), ESTADO_ACTIVA);

        ConversacionChatbotDocument conversacion = activa.orElseGet(ConversacionChatbotDocument::new);
        String intencion = detectarIntencion(request.getMensaje());
        String estado = resolverEstadoConversacion(intencion);
        String respuestaBot = resolverRespuesta(request.getMensaje(), intencion);

        conversacion.setUsuarioId(request.getUsuarioId());
        conversacion.setUltimoMensajeUsuario(request.getMensaje());
        conversacion.setIntencionDetectada(intencion);
        conversacion.setRespuestaBot(respuestaBot);
        conversacion.setEstadoConversacion(estado);
        conversacion.setUpdatedAt(Instant.now());

        ConversacionChatbotDocument saved = conversacionChatbotRepository.save(conversacion);
        return ChatbotQueryResponse.builder()
                .conversacionId(saved.getId())
                .usuarioId(saved.getUsuarioId())
                .mensajeUsuario(saved.getUltimoMensajeUsuario())
                .intencionDetectada(saved.getIntencionDetectada())
                .respuestaBot(saved.getRespuestaBot())
                .estadoConversacion(saved.getEstadoConversacion())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    private String resolverRespuesta(String mensaje, String intencion) {
        if (iaProperties.isEnabled()) {
            // Gancho: cuando exista LlmClient, usar mensaje + intencion como prompt
        }
        return chatbotRespuestaService.respuestaPorIntencion(intencion);
    }

    private String detectarIntencion(String mensaje) {
        String texto = mensaje.toLowerCase(Locale.ROOT);
        if (texto.contains("hola") || texto.contains("buenas")) {
            return "SALUDO";
        }
        if (texto.contains("ayuda") || texto.contains("soporte")) {
            return "AYUDA";
        }
        if (texto.contains("progreso") || texto.contains("avance")) {
            return "PROGRESO";
        }
        if (texto.contains("gracias") || texto.contains("adios")) {
            return "CIERRE";
        }
        return "CONSULTA_GENERAL";
    }

    private String resolverEstadoConversacion(String intencion) {
        if ("CIERRE".equals(intencion)) {
            return ESTADO_CERRADA;
        }
        return ESTADO_ACTIVA;
    }
}
