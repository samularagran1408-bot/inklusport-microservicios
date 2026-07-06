package com.inklusport.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analisis_biomecanicos")
public class BiomechanicalAnalysis {

    @Id
    private String id;
    private String usuarioId;
    private String tipoDiscapacidad;
    private Integer rangoMovimiento;
    private Integer simetria;
    private Integer estabilidad;
    private Double puntaje;
    private List<String> recomendaciones;
    private String analisisIA;
    private LocalDateTime createdAt;
}
