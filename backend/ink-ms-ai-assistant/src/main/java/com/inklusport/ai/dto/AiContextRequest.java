package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiContextRequest {

    @NotBlank(message = "usuarioId es obligatorio")
    private String usuarioId;

    private String input;
    private Map<String, Object> context;
}
