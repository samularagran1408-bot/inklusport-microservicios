package com.inklusport.sports.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "El evento es obligatorio")
    private String eventId;

    @NotBlank(message = "El usuario es obligatorio")
    private String userId;
}
