package com.inklusport.ai.dto;

import lombok.Data;
import java.util.Map;

@Data
public class BiomechanicalRequest {
    
    private Map<String, Object> movementData;
    
    private String ejercicioNombre;
    
    private String disabilityType;
}