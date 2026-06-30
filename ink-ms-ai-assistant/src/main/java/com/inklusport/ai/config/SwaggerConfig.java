package com.inklusport.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inklusport AI Assistant API")
                        .description("API del chatbot con Google Gemini para deportes adaptados")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Inklusport Team")
                                .email("support@inklusport.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://apache.org/licenses/LICENSE-2.0")));
    }
}