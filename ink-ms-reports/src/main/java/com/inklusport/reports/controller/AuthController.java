package com.inklusport.reports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inklusport.reports.security.JwtProvider;

@RestController
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/token")
    public String generateToken() {

        return jwtProvider.generateToken("admin");
    }
}