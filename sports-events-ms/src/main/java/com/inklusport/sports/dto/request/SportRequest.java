package com.inklusport.sports.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SportRequest {

    @NotBlank(message = "El nombre del deporte es obligatorio")
    @Size(max = 100)
    private String name;

    private String description;
}
