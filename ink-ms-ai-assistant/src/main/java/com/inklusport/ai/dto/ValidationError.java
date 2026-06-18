package com.inklusport.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para errores de validación de campos
 * Usado en ErrorResponse para detalles específicos de validación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    
    private String field;
    private String message;
    private Object rejectedValue;
    private String errorCode;
    
    // Constructor útil para crear errores rápidamente
    public static ValidationError of(String field, String message) {
        return ValidationError.builder()
                .field(field)
                .message(message)
                .build();
    }
    
    public static ValidationError of(String field, String message, Object rejectedValue) {
        return ValidationError.builder()
                .field(field)
                .message(message)
                .rejectedValue(rejectedValue)
                .build();
    }
    
    public static ValidationError of(String field, String message, String errorCode) {
        return ValidationError.builder()
                .field(field)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}