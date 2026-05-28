package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "El ID de la solicitud es obligatorio")
    private String approvalId;
    
    @NotBlank(message = "La decisión es obligatoria")
    private String decision; /** approved, rejected */
    
    private String notes;
}