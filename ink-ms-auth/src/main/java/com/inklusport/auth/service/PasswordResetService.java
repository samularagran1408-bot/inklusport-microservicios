package com.inklusport.auth.service;

import com.inklusport.auth.config.InvalidResetTokenException;
import com.inklusport.auth.dto.ForgotPasswordRequest;
import com.inklusport.auth.dto.ForgotPasswordResponse;
import com.inklusport.auth.dto.ResetPasswordRequest;
import com.inklusport.auth.dto.ResetPasswordResponse;
import com.inklusport.auth.entity.AuthUser;
import com.inklusport.auth.entity.PasswordResetToken;
import com.inklusport.auth.repository.AuthUserRepository;
import com.inklusport.auth.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

  private final AuthUserRepository authUserRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

  @Value("${server.url:http://localhost:3001}")
  private String serverUrl;

  @Value("${app.password-reset.mail-enabled:false}")
  private boolean mailEnabled;

  @Value("${app.password-reset.expose-token-in-response:false}")
  private boolean exposeTokenInResponse;

  @Value("${app.password-reset.token-expiry-hours:24}")
  private int tokenExpiryHours;

  @Transactional
  public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
    String resetToken = null;

    AuthUser user = authUserRepository.findByEmail(request.getEmail()).orElse(null);

    if (user != null) {
      tokenRepository.deleteByUserId(user.getId());

      resetToken = UUID.randomUUID().toString();
      PasswordResetToken token = new PasswordResetToken();
      token.setUserId(user.getId());
      token.setToken(resetToken);
      token.setExpiresAt(LocalDateTime.now().plusHours(tokenExpiryHours));
      token.setUsed(false);

      tokenRepository.save(token);
      log.info("Token de recuperación generado para: {}", user.getEmail());
    } else {
      log.info("Solicitud de recuperación para email no registrado: {}", request.getEmail());
    }

    if (resetToken != null) {
      sendResetEmailSafely(request.getEmail(), resetToken);
    }

    ForgotPasswordResponse.ForgotPasswordResponseBuilder builder = ForgotPasswordResponse.builder()
            .message("Si el email está registrado, recibirás instrucciones para restablecer tu contraseña.");

    if (exposeTokenInResponse && resetToken != null) {
      builder.resetToken(resetToken);
    }

    return builder.build();
  }

  @Transactional
  public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
    PasswordResetToken token = tokenRepository.findByTokenAndUsedFalse(request.getToken())
            .orElseThrow(() -> new InvalidResetTokenException("Token inválido o expirado"));

    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidResetTokenException("Token expirado");
    }

    AuthUser user = authUserRepository.findById(token.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    authUserRepository.save(user);

    token.setUsed(true);
    tokenRepository.save(token);

    log.info("Contraseña restablecida para usuario: {}", user.getEmail());

    return ResetPasswordResponse.builder()
            .message("Contraseña actualizada correctamente")
            .build();
  }

  private void sendResetEmailSafely(String email, String token) {
    if (!mailEnabled) {
      log.info("Correo deshabilitado. Token de recuperación (Postman): {}", token);
      return;
    }

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(email);
      message.setSubject("Recuperación de contraseña - InkluSport");
      message.setText(
              "Usa este token en Postman (POST /api/auth/reset-password):\n\n"
                      + token
                      + "\n\nExpira en " + tokenExpiryHours + " horas."
      );
      mailSender.send(message);
    } catch (Exception e) {
      log.warn("No se pudo enviar el correo de recuperación a {}: {}", email, e.getMessage());
    }
  }
}
