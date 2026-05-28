package com.inklusport.ia.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "planes_entrenamiento")
@CompoundIndex(name = "usuario_entrenador_idx", def = "{'usuario_id': 1, 'entrenador_id': 1}")
@Data
public class PlanEntrenamientoDocument {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("entrenador_id")
    private String entrenadorId;

    private List<EjercicioAdaptado> ejercicios;

    @Field("updated_at")
    private Instant updatedAt;

    @Data
    public static class EjercicioAdaptado {
        @Field("nombre_ejercicio")
        private String nombreEjercicio;

        private List<String> adaptaciones;
    }
}
