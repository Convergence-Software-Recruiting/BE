package com.example.convergencesoftwarerecruitingbe.domain.admin.account.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.ChangePasswordRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.ChangeUsernameRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.VerifyPasswordRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.account.service.AdminAccountService;
import com.example.convergencesoftwarerecruitingbe.global.auth.cookie.CookieProperties;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtConstants;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/account")
public class AdminAccountController implements AdminAccountControllerDocs {

    private final AdminAccountService adminAccountService;
    private final JwtService jwtService;
    private final CookieProperties cookieProperties;

    @Override
    @PostMapping("/verify-password")
    public ResponseEntity<Void> verifyPassword(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody VerifyPasswordRequest request
    ) {
        adminAccountService.verifyPassword(principal.getId(), request.getCurrentPassword());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        adminAccountService.changePassword(
                principal.getId(),
                request.getCurrentPassword(),
                request.getNewPassword()
        );
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/username")
    public ResponseEntity<Void> changeUsername(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody ChangeUsernameRequest request
    ) {
        String token = adminAccountService.changeUsernameAndIssueNewToken(
                principal.getId(),
                request.getCurrentPassword(),
                request.getNewUsername()
        );

        ResponseCookie cookie = buildAccessTokenCookie(token, jwtService.getExpirationSeconds());

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    private ResponseCookie buildAccessTokenCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .sameSite("Lax")
                .build();
    }
}
