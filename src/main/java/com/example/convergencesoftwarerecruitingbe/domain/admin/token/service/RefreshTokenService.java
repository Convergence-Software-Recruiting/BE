package com.example.convergencesoftwarerecruitingbe.domain.admin.token.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.Admin;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.AdminRole;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.dto.response.TokenRefreshResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.entity.AdminRefreshToken;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.repository.AdminRefreshTokenRepository;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtConstants;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final AdminRefreshTokenRepository repository;
    private final JwtService jwtService;

    public String issueForPrincipal(AdminPrincipal principal) {
        Admin admin = toAdmin(principal);
        String refreshToken = jwtService.generateRefreshToken(admin);
        Instant expiresAt = Instant.now().plusSeconds(jwtService.getRefreshExpirationSeconds());
        AdminRefreshToken stored = repository.findById(principal.getId())
                .orElseGet(() -> new AdminRefreshToken(principal.getId(), refreshToken, expiresAt));
        stored.rotate(refreshToken, expiresAt);
        repository.save(stored);
        return refreshToken;
    }

    public TokenRefreshResponse refresh(String refreshToken) {
        AdminRefreshToken stored = repository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> unauthorized("Invalid refresh token"));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            repository.delete(stored);
            throw unauthorized("Refresh token expired");
        }

        Claims claims;
        try {
            claims = jwtService.parseClaims(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            repository.delete(stored);
            throw unauthorized("Invalid refresh token");
        }

        Long adminId = Long.valueOf(claims.getSubject());
        if (!stored.getAdminId().equals(adminId)) {
            repository.delete(stored);
            throw unauthorized("Invalid refresh token");
        }

        String tokenType = claims.get(JwtConstants.TOKEN_TYPE_CLAIM, String.class);
        if (!JwtConstants.TOKEN_TYPE_REFRESH.equals(tokenType)) {
            repository.delete(stored);
            throw unauthorized("Invalid refresh token");
        }

        String username = claims.get(JwtService.CLAIM_USERNAME, String.class);
        String roleValue = claims.get(JwtService.CLAIM_ROLE, String.class);
        AdminRole role = AdminRole.valueOf(roleValue);
        AdminPrincipal principal = new AdminPrincipal(adminId, username, role);
        Admin admin = toAdmin(principal);
        String newAccessToken = jwtService.generateAccessToken(admin);
        String newRefreshToken = jwtService.generateRefreshToken(admin);
        Instant newExpiresAt = Instant.now().plusSeconds(jwtService.getRefreshExpirationSeconds());
        stored.rotate(newRefreshToken, newExpiresAt);
        repository.save(stored);

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }

    private Admin toAdmin(AdminPrincipal principal) {
        return Admin.builder()
                .id(principal.getId())
                .username(principal.getUsername())
                .role(principal.getRole())
                .passwordHash("N/A")
                .build();
    }

    private ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    public void revokeByAdminId(Long adminId) {
        repository.deleteById(adminId);
    }
}
