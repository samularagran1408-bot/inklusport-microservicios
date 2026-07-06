package com.inklusport.ai.ai;

import org.springframework.stereotype.Service;

@Service
public class InjuryPredictor {
    public String predict(String input) {
        return "Predicción de lesión: " + input;
    }
}
