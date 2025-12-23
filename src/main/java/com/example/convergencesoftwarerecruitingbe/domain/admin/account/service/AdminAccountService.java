package com.example.convergencesoftwarerecruitingbe.domain.admin.account.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.repository.AdminRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.Admin;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAccountService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void verifyPassword(Long adminId, String currentPassword) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(this::unauthorized);

        if (!passwordEncoder.matches(currentPassword, admin.getPasswordHash())) {
            throw unauthorized();
        }
    }

    @Transactional
    public void changePassword(Long adminId, String currentPassword, String newPassword) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(this::unauthorized);

        if (!passwordEncoder.matches(currentPassword, admin.getPasswordHash())) {
            throw unauthorized();
        }

        admin.changePasswordHash(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public String changeUsernameAndIssueNewToken(Long adminId, String currentPassword, String newUsername) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(this::unauthorized);

        if (!passwordEncoder.matches(currentPassword, admin.getPasswordHash())) {
            throw unauthorized();
        }

        if (newUsername.equals(admin.getUsername())) {
            return jwtService.generateToken(admin);
        }

        if (adminRepository.existsByUsername(newUsername)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        admin.changeUsername(newUsername);

        return jwtService.generateToken(admin);
    }

    private ResponseStatusException unauthorized() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials or admin not found");
    }
}