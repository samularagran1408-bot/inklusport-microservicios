package com.inklusport.ia.repository;

import com.inklusport.ia.document.AnalisisBiomecanicoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnalisisBiomecanicoRepository extends MongoRepository<AnalisisBiomecanicoDocument, String> {

    List<AnalisisBiomecanicoDocument> findByUsuarioIdOrderByFechaAnalisisDesc(String usuarioId);
}
