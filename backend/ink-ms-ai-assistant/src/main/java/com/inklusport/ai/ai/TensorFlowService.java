package com.inklusport.ai.ai;

import org.springframework.stereotype.Service;

@Service
public class TensorFlowService {
    public String infer(String input) {
        return "Inferencia de modelo: " + input;
    }
}
