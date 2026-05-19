package com.inklusport.sports.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {

    @NotBlank(message = "El deporte es obligatorio")
    private String sportId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String title;

    private String description;

    @Size(max = 255)
    private String location;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser futura")
    private LocalDateTime endDate;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "Debe haber al menos un participante")
    private Integer maxParticipants;
}
