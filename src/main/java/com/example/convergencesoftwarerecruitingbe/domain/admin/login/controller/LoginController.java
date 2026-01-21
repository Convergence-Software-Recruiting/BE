package com.example.convergencesoftwarerecruitingbe.domain.admin.login.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.request.LoginRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.response.LoginResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.service.AdminAuthService;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.dto.request.TokenRefreshRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.dto.response.LoginTokenResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.dto.response.TokenRefreshResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.token.service.RefreshTokenService;
import com.example.convergencesoftwarerecruitingbe.global.auth.cookie.CookieProperties;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtConstants;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "admin-login-controller", description = "관리자 로그인 관련 API")
@RequestMapping("/api/admin")
public class LoginController {

    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CookieProperties cookieProperties;

    @Operation(
            summary = "관리자 로그인",
            description = "성공 시 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)를 Set-Cookie로 내려줍니다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String accessToken = adminAuthService.login(request.getUsername(), request.getPassword());
        AdminPrincipal principal = jwtService.parseToken(accessToken);
        String refreshToken = refreshTokenService.issueForPrincipal(principal);
        ResponseCookie cookie = buildAccessTokenCookie(accessToken, jwtService.getAccessExpirationSeconds());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginTokenResponse(accessToken, refreshToken));
    }

    @Operation(
            summary = "관리자 로그아웃",
            description = "토큰 쿠키(ADMIN_ACCESS_TOKEN)를 만료(Max-Age=0)시킵니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        revokeRefreshTokenIfPossible(request);
        ResponseCookie cookie = buildAccessTokenCookie("", 0);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @Operation(
            summary = "내 정보 조회",
            description = "로그인으로 발급된 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)가 필요합니다."
    )
    @GetMapping("/me")
    public LoginResponse me(@Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal) {
        return LoginResponse.from(principal);
    }

    @Operation(
            summary = "토큰 재발급",
            description = "refreshToken으로 accessToken/refreshToken을 재발급합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = refreshTokenService.refresh(request.getRefreshToken());
        ResponseCookie cookie = buildAccessTokenCookie(response.getAccessToken(), jwtService.getAccessExpirationSeconds());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
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

    private void revokeRefreshTokenIfPossible(HttpServletRequest request) {
        String token = extractAccessToken(request);
        if (!StringUtils.hasText(token)) {
            return;
        }

        try {
            AdminPrincipal principal = jwtService.parseToken(token);
            refreshTokenService.revokeByAdminId(principal.getId());
        } catch (Exception ignored) {
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (JwtConstants.ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
