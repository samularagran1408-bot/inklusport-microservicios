package com.inklusport.reports.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "inklusportsecretkeyinklusportsecretkey".getBytes());

    public String generateToken(String username) {

       return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
        .signWith(secretKey)
        .compact();
    }
}