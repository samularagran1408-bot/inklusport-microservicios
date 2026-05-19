package com.inklusport.auth.service;

import com.inklusport.auth.dto.ForgotPasswordRequest;
import com.inklusport.auth.dto.ResetPasswordRequest;
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

  /**
   * Restablece la contraseña de un usuario
   * @param request
   */
  @Transactional
  public void forgotPassword(ForgotPasswordRequest request) {
    AuthUser user = authUserRepository.findByEmail(request.getEmail())
              .orElse(null);

    if (user == null) {
        log.info("Solicitud de recuperación para email no registrado: {}", request.getEmail());
        return;
    }

    tokenRepository.deleteByUserId(user.getId());

    String tokenValue = UUID.randomUUID().toString();
    PasswordResetToken token = new PasswordResetToken();
    token.setUserId(user.getId());
    token.setToken(tokenValue);
    token.setExpiresAt(LocalDateTime.now().plusHours(24));
    token.setUsed(false);

    tokenRepository.save(token);

    sendResetEmail(user.getEmail(), tokenValue);

    log.info("Token de recuperación generado para: {}", user.getEmail());
  }

  @Transactional
  public void resetPassword(ResetPasswordRequest request) {
    PasswordResetToken token = tokenRepository.findByTokenAndUsedFalse(request.getToken())
                        .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));
    
    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expirado");
    }

    AuthUser user = authUserRepository.findById(token.getUserId()) 
              .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
    user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    authUserRepository.save(user);

    token.setUsed(true);
    tokenRepository.save(token);
  }

  /**
   * Restablece la contraseña utilizando el token
   * @param request
   * @param email
   * @param token
   */
  private void sendResetEmail(String email, String token) {
        String resetUrl = serverUrl + "/api/auth/reset-password?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña - InkluSport");
        message.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" + resetUrl + "\n\nEste enlace expirará en 24 horas.\n\nSi no solicitaste este cambio, ignora este mensaje.");
        
        mailSender.send(message);
    }
}
