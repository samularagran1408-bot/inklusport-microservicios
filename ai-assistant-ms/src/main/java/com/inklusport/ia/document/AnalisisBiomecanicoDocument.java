package com.inklusport.ia.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Document(collection = "analisis_biomecanicos")
@Data
public class AnalisisBiomecanicoDocument {

    @Id
    private String id;

    @Indexed
    @Field("usuario_id")
    private String usuarioId;

    @Field("tipo_discapacidad")
    private String tipoDiscapacidad;

    @Field("rango_movimiento")
    private Integer rangoMovimiento;

    private Integer simetria;

    private Integer estabilidad;

    private Double puntaje;

    private List<String> recomendaciones;

    @Indexed
    @Field("fecha_analisis")
    private Instant fechaAnalisis;
}
