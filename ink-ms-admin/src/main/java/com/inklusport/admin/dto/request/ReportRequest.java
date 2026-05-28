package com.inklusport.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReportRequest {
    @NotBlank(message = "El nombre del reporte es obligatorio")
    private String name;
    
    @NotBlank(message = "El tipo de reporte es obligatorio")
    private String type;
    
    @NotBlank(message = "La expresión CRON es obligatoria")
    private String scheduleCron;
    
    private Object parameters;
    
    private List<String> recipients;
}