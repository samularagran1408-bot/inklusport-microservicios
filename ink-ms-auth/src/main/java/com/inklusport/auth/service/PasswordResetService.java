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

/**
 * Servicio de recuperación de contraseña.
 * Maneja generación, expiración y uso único de tokens.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final AuthUserRepository authUserRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; 

    @Value("${app.password-reset.mail-enabled:true}")
    private boolean mailEnabled;

    @Value("${app.password-reset.expose-token-in-response:false}")
    private boolean exposeTokenInResponse;

    @Value("${app.password-reset.token-expiry-hours:24}")
    private int tokenExpiryHours;

    /**
     * Genera token de recuperación si el correo existe y opcionalmente envía email.
     * La respuesta es neutra para no exponer si el correo está registrado.
     */
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

        // Enviar correo solo si se generó token y el email está habilitado
        if (resetToken != null && mailEnabled) {
            emailService.sendPasswordResetEmail(request.getEmail(), resetToken, tokenExpiryHours);
        } else if (resetToken != null && !mailEnabled) {
            log.info("Correo deshabilitado. Token de recuperación (Postman): {}", resetToken);
        }

        ForgotPasswordResponse.ForgotPasswordResponseBuilder builder = ForgotPasswordResponse.builder()
                .message("Si el email está registrado, recibirás instrucciones para restablecer tu contraseña.");

        if (exposeTokenInResponse && resetToken != null) {
            builder.resetToken(resetToken);
        }

        return builder.build();
    }

    /**
     * Valida token, actualiza contraseña y marca token como usado.
     */
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
}