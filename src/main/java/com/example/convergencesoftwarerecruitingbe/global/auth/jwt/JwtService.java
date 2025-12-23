package com.example.convergencesoftwarerecruitingbe.global.auth.jwt;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.Admin;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.AdminRole;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLE = "role";

    private final JwtProperties properties;
    private final SecretKey secretKey;

    @PostConstruct
    void validateSecret() {
        byte[] keyBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("jwt.secret must be at least 256 bits (32+ ASCII chars) for HS256");
        }
    }

    public String generateToken(Admin admin) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(properties.getExpirationSeconds());

        return Jwts.builder()
                .subject(String.valueOf(admin.getId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim(CLAIM_USERNAME, admin.getUsername())
                .claim(CLAIM_ROLE, admin.getRole().name())
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public AdminPrincipal parseToken(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long adminId = Long.valueOf(claims.getSubject());
        String username = claims.get(CLAIM_USERNAME, String.class);
        String roleValue = claims.get(CLAIM_ROLE, String.class);
        AdminRole role = AdminRole.valueOf(roleValue);

        return new AdminPrincipal(adminId, username, role);
    }

    public long getExpirationSeconds() {
        return properties.getExpirationSeconds();
    }
}
