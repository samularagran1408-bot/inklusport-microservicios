package com.inklusport.ia.repository;

import com.inklusport.ia.document.PlanEntrenamientoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlanEntrenamientoRepository extends MongoRepository<PlanEntrenamientoDocument, String> {

    Optional<PlanEntrenamientoDocument> findByUsuarioIdAndEntrenadorId(String usuarioId, String entrenadorId);
}
