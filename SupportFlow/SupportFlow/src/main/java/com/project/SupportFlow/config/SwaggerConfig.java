package com.project.SupportFlow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {

        final String securityScheme = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("SupportFlow API")
                        .version("1.0")
                        .description("API de suporte automatizado com IA"))
                .schemaRequirement(
                        securityScheme,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")

                );

    }
}
