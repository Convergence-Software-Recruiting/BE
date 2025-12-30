package com.example.convergencesoftwarerecruitingbe.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("https://recruit.bluerack.org");
        return new OpenAPI().servers(List.of(server));
    }
}