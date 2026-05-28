package com.inklusport.ia.controller;

import com.inklusport.ia.dto.request.ChatbotQueryRequest;
import com.inklusport.ia.dto.response.ChatbotQueryResponse;
import com.inklusport.ia.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chatbot del modulo IA.
 * Hoy detecta la intencion con palabras clave y guarda la conversacion en Mongo.
 * Mas adelante se puede conectar un LLM (OpenAI, Ollama, etc) sin cambiar esta ruta.
 */
@RestController
@RequestMapping("/api/ia/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    /**
     * Recibe un mensaje del usuario y devuelve la intencion detectada + estado de la conversacion.
     */
    @PostMapping
    public ResponseEntity<ChatbotQueryResponse> procesarMensaje(
            @Valid @RequestBody ChatbotQueryRequest request) {
        return ResponseEntity.ok(chatbotService.procesarMensaje(request));
    }
}
