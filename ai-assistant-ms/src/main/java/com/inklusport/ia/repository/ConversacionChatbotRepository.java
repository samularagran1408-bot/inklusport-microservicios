package com.inklusport.ia.repository;

import com.inklusport.ia.document.ConversacionChatbotDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConversacionChatbotRepository extends MongoRepository<ConversacionChatbotDocument, String> {

    Optional<ConversacionChatbotDocument> findByUsuarioIdAndEstadoConversacionIgnoreCase(
            String usuarioId,
            String estadoConversacion
    );
}
