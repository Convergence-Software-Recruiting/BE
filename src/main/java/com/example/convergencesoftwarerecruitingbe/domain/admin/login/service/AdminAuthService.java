package com.example.convergencesoftwarerecruitingbe.domain.admin.login.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.repository.AdminRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.Admin;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> unauthorized("Invalid username or password"));

        if (!passwordEncoder.matches(password, admin.getPasswordHash())) {
            throw unauthorized("Invalid username or password");
        }

        return jwtService.generateToken(admin);
    }

    private ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }
}
