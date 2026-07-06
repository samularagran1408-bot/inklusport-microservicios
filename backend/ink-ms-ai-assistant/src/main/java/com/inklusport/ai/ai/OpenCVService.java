package com.inklusport.ai.ai;

import org.springframework.stereotype.Service;

@Service
public class OpenCVService {
    public String processVision(String input) {
        return "Procesamiento por visión: " + input;
    }
}
