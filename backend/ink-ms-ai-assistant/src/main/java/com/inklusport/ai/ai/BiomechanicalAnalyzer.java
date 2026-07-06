package com.inklusport.ai.ai;

import org.springframework.stereotype.Service;

@Service
public class BiomechanicalAnalyzer {
    public String analyze(String input) {
        return "Análisis biomecánico: " + input;
    }
}
