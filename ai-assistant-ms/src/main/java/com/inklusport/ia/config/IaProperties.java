package com.inklusport.ia.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Config para cuando conecten un LLM (OpenAI, Ollama, etc).
 * Por defecto ia.enabled=false: el chat usa respuestas fijas, no modelo externo.
 */
@Data
@ConfigurationProperties(prefix = "ia")
public class IaProperties {

    private boolean enabled = false;
    private String provider = "none";
    private String baseUrl = "http://localhost:11434";
    private String model = "llama3";
    private String apiKey = "";
}
