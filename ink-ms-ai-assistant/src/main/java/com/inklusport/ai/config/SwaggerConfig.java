package com.inklusport.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String port;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inklusport AI Service API")
                        .description("API para el microservicio de IA de Inklusport")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Inklusport Team")
                                .email("support@inklusport.com")
                                .url("https://inklusport.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + port + "/api/ai")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.inklusport.com/ai")
                                .description("Servidor de Producción")
                ));
    }
}