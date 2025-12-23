package com.example.convergencesoftwarerecruitingbe.global.auth.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank
    private String secret;

    @Min(1)
    private long expirationSeconds;
}
