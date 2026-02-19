package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "admin")
public class AdminMailProperties {
    private String email;
}
