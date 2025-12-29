package com.example.convergencesoftwarerecruitingbe.domain.admin.login.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.repository.AdminRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.Admin;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.AdminRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final AdminRepository adminRepository;
    private final AdminSeedProperties properties;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void seedAdmin() {
        if (!StringUtils.hasText(properties.getUsername()) || !StringUtils.hasText(properties.getPassword())) {
            log.warn("Admin seed skipped: admin.seed.username/password not configured");
            return;
        }

        if (adminRepository.existsByUsername(properties.getUsername())) {
            log.info("Admin seed skipped: '{}' already exists", properties.getUsername());
            return;
        }

        Admin admin = Admin.builder()
                .username(properties.getUsername())
                .passwordHash(passwordEncoder.encode(properties.getPassword()))
                .role(AdminRole.ADMIN)
                .build();
        adminRepository.save(admin);
        log.info("Seeded admin user '{}'", admin.getUsername());
    }
}
