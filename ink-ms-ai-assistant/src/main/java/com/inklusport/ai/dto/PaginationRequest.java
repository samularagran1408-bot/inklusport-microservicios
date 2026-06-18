package com.inklusport.ai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Min(value = 0, message = "La página no puede ser negativa")
    @Builder.Default
    private Integer page = 0;

    @Min(value = 1, message = "El tamaño debe ser al menos 1")
    @Max(value = 100, message = "El tamaño no puede exceder 100")
    @Builder.Default
    private Integer size = 20;

    private String sortBy = "fecha";

    @Builder.Default
    private String sortDirection = "DESC";
}