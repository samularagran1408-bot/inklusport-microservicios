package com.inklusport.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía correo HTML de recuperación de contraseña de forma asíncrona.
     */
    @Async
    public void sendPasswordResetEmail(String to, String resetToken, int expiryHours) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String resetUrl = "http://localhost:3001/api/auth/reset-password?token=" + resetToken;

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña - InkluSport");
            helper.setText(buildEmailContent(resetUrl, expiryHours), true);

            mailSender.send(message);
            log.info("Correo de recuperación enviado a: {}", to);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", to, e.getMessage());
        }
    }

    /**
     * Construye el HTML del correo de recuperación.
     */
    private String buildEmailContent(String resetToken, int expiryHours) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"></head>
            <body style="font-family: Arial, sans-serif;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #1E3A8A;">InkluSport</h2>
                    <h3>Recuperación de contraseña</h3>
                    <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                    <p>Usa el siguiente token para restablecer tu contraseña:</p>
                    <div style="background-color: #f4f4f4; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <code style="font-size: 18px; font-weight: bold;">%s</code>
                    </div>
                    <p>Para restablecer tu contraseña, haz una petición POST a:</p>
                    <code>http://localhost:3001/api/auth/reset-password</code>
                    <p>Con el siguiente cuerpo:</p>
                    <pre style="background-color: #f4f4f4; padding: 10px; border-radius: 5px;">
    {
        "token": "%s",
        "newPassword": "tu_nueva_contraseña"
    }
                    </pre>
                    <p>Este token expirará en <strong>%d horas</strong>.</p>
                    <p>Si no solicitaste este cambio, ignora este mensaje.</p>
                    <hr>
                    <p style="font-size: 12px; color: #666;">InkluSport - Deporte para todos</p>
                </div>
            </body>
            </html>
            """.formatted(resetToken, resetToken, expiryHours);
    }
}