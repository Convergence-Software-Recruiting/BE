package com.example.convergencesoftwarerecruitingbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ConvergenceSoftwareRecruitingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConvergenceSoftwareRecruitingBeApplication.class, args);
    }

}
