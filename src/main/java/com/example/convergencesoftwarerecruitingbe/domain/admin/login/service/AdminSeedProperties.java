package com.example.convergencesoftwarerecruitingbe.domain.admin.login.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Profile("local")
@ConfigurationProperties(prefix = "admin.seed")
public class AdminSeedProperties {
    private String username;
    private String password;
}
