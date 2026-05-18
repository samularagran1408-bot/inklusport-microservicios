package com.inklusport.auth.controller;

import com.inklusport.auth.dto.request.ForgotPasswordRequest;
import com.inklusport.auth.dto.request.LoginRequest;
import com.inklusport.auth.dto.request.RegisterRequest;
import com.inklusport.auth.dto.request.ResetPasswordRequest;
import com.inklusport.auth.dto.response.AuthResponse;
import com.inklusport.auth.dto.response.ErrorResponse;
import com.inklusport.auth.security.JwtTokenProvider;
import com.inklusport.auth.service.AuthService;
import com.inklusport.auth.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  /** Se inyectasn los service de autenticación y de reseteo de password */
  private final AuthService authService;
  private final PasswordResetService passwordResetService;
  private final JwtTokenProvider jwtTokenProvider;
  
  @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
      try {
          AuthResponse response = authService.register(request, getClientIp(httpRequest));
          return ResponseEntity.ok(response);
      } catch (Exception e) {
          return buildErrorResponse(e, "/api/auth/register");
      }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
      try {
          AuthResponse response = authService.login(request, getClientIp(httpRequest));
          return ResponseEntity.ok(response);
      } catch (Exception e) {
          return buildErrorResponse(e, "/api/auth/login");
      }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
      return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
      try {
        passwordResetService.forgotPassword(request);
        return ResponseEntity.ok().build();
      } catch (Exception e) {
          e.printStackTrace();  
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(Map.of("message", e.getMessage())); 
      }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
      passwordResetService.resetPassword(request);
      return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
      if (authHeader == null ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Token no proporcionado o formato inválido");
      }
      
      /** Extraer token (Bearer + espacio = 7 caracteres) */
      String token = authHeader.substring(7);
      
      /** Validar token */ 
      if (jwtTokenProvider.validateToken(token)) {
          return ResponseEntity.ok(Map.of("valid", true));
      } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body("Token inválido o expirado");
      }
    }

    private String getClientIp(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (ip == null || ip.isEmpty()) {
          ip = request.getRemoteAddr();
      }
      return ip;
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(e.getMessage())
                .path(path)
                .build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
  
}
