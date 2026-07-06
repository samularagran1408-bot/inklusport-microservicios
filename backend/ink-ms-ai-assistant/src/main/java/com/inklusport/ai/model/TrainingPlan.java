package com.inklusport.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "planes_entrenamiento")
public class TrainingPlan {

    @Id
    private String id;
    private String usuarioId;
    private String entrenadorId;
    private String tipoDiscapacidad;
    private String objetivo;
    private String planGenerado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
