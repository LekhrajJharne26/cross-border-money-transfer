package com.crossborder.moneytransfer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/** Supplies OpenAPI metadata and the reusable Bearer token scheme. */
public class OpenApiConfig {
    @Bean OpenAPI crossBorderMoneyTransferOpenApi() {
        return new OpenAPI().info(new Info().title("Cross Border Money Transfer API").version("v1"))
                .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
