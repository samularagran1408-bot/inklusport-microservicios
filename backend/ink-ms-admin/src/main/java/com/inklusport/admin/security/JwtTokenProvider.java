package com.inklusport.admin.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Proveedor de tokens JWT para autenticacion y autorizacion en el microservicio de administracion.
 * Maneja la generacion, validacion y extraccion de informacion de tokens JWT.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    /**
     * Clave secreta para firmar los tokens JWT.
     * Se carga desde la configuracion (application.yml).
     */
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    /**
     * Tiempo de expiracion del token JWT en milisegundos.
     * Se carga desde la configuracion (application.yml).
     */
    @Value("${security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Obtiene la clave secreta para firmar los tokens.
     * @return Clave HMAC SHA
     */
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario (sin roles especificos).
     * @param email Email del usuario
     * @return Token JWT generado
     */
    public String generateToken(String email) {
        return generateToken(email, List.of());
    }

    /**
     * Genera un token JWT para un usuario con roles asignados.
     * @param email Email del usuario
     * @param roles Lista de roles del usuario
     * @return Token JWT generado
     */
    public String generateToken(String email, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extrae el email del usuario desde un token JWT valido.
     * @param token Token JWT a procesar
     * @return Email del usuario
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Extrae los roles del usuario desde un token JWT valido.
     * @param token Token JWT a procesar
     * @return Lista de roles del usuario
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    /**
     * Valida que un token JWT sea correcto, no este expirado y este correctamente firmado.
     * @param token Token JWT a validar
     * @return true si el token es valido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token JWT vacio: {}", e.getMessage());
        }
        return false;
    }
}
