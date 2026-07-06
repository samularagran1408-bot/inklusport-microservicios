package com.inklusport.ai.ai;

import org.springframework.stereotype.Service;

@Service
public class FatigueDetector {
    public String detect(String input) {
        return "Detección de fatiga: " + input;
    }
}
