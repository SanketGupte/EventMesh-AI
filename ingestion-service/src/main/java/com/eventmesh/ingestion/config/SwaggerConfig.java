package com.eventmesh.ingestion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI Configuration for Ingestion Service.
 * Provides API documentation through Swagger UI.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Development Server"),
                new Server().url("https://api.eventmesh.prod").description("Production Server")
            ))
            .info(new Info()
                .title("EventMesh AI - Ingestion Service")
                .description("REST API for event ingestion and API key management")
                .version("0.0.1")
                .contact(new Contact()
                    .name("EventMesh Team")
                    .email("support@eventmesh.io")
                    .url("https://eventmesh.io"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT")));
    }
}

