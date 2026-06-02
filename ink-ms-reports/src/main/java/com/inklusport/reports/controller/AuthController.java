package com.inklusport.reports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inklusport.reports.security.JwtProvider;

/**
 * Endpoint utilitario para generar un token de pruebas en reports.
 */
@RestController
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Genera un JWT de prueba para el usuario "admin".
     */
    @GetMapping("/token")
    public String generateToken() {

        return jwtProvider.generateToken("admin");
    }
}