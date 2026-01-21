package com.example.convergencesoftwarerecruitingbe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private final Environment environment;

    public SwaggerConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI()
                .components(new Components().addSecuritySchemes("BearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));

        if (isProd()) {
            Server server = new Server();
            server.setUrl("https://recruit.bluerack.org");
            openAPI.servers(List.of(server));
        }

        return openAPI;
    }

    private boolean isProd() {
        for (String profile : environment.getActiveProfiles()) {
            if ("prod".equals(profile)) {
                return true;
            }
        }
        return false;
    }
}
