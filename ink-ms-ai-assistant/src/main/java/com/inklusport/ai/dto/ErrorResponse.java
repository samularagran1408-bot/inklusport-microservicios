package com.inklusport.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error detallada")
public class ErrorResponse {

    @Schema(description = "Timestamp del error", example = "2024-01-15T10:30:00.123")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private Integer status;

    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje de error", example = "El mensaje no puede estar vacío")
    private String message;

    @Schema(description = "Código de error personalizado", example = "VALIDATION_ERROR")
    private String errorCode;

    @Schema(description = "Ruta donde ocurrió el error", example = "/api/ai/chat/message")
    private String path;

    @Schema(description = "ID de trazabilidad", example = "abc-123-def-456")
    private String traceId;

    @Schema(description = "Errores de validación por campo")
    private List<ValidationError> validationErrors;

    @Schema(description = "Información adicional del error")
    private Map<String, String> additionalInfo;
}