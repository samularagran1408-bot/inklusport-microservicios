package com.inklusport.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

  @NotBlank(message = "El token es obligatorio")
  private String token;

  @NotBlank(message = "La nueva contraseña es obligatoria")
  @Size(min = 6, message = "La contraseña debe tener por lo menos 6 caracteres")
  private String newPassword;
}
