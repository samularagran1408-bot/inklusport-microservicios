package com.inklusport.sports.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DisabilityRequest {

    @NotBlank(message = "El nombre de la discapacidad es obligatorio")
    @Size(max = 100)
    private String name;

    private String description;
}
