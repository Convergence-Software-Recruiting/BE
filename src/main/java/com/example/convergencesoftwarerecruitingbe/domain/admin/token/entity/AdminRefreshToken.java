package com.example.convergencesoftwarerecruitingbe.domain.admin.token.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "admin_refresh_tokens")
public class AdminRefreshToken {

    @Id
    private Long adminId;

    @Column(nullable = false, unique = true, length = 512)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiresAt;

    public AdminRefreshToken(Long adminId, String refreshToken, Instant expiresAt) {
        this.adminId = adminId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public void rotate(String refreshToken, Instant expiresAt) {
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }
}
