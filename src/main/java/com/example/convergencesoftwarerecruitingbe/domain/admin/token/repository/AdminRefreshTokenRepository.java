package com.example.convergencesoftwarerecruitingbe.domain.admin.token.repository;

import com.example.convergencesoftwarerecruitingbe.domain.admin.token.entity.AdminRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRefreshTokenRepository extends JpaRepository<AdminRefreshToken, Long> {
    Optional<AdminRefreshToken> findByRefreshToken(String refreshToken);
}
