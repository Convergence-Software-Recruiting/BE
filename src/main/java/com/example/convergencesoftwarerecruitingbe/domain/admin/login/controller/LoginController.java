package com.example.convergencesoftwarerecruitingbe.domain.admin.login.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.request.LoginRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.response.LoginResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.service.AdminAuthService;
import com.example.convergencesoftwarerecruitingbe.global.auth.cookie.CookieProperties;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtConstants;
import com.example.convergencesoftwarerecruitingbe.global.auth.jwt.JwtService;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class LoginController implements
       LoginControllerDocs {

    private final AdminAuthService adminAuthService;
    private final JwtService jwtService;
    private final CookieProperties cookieProperties;

    @Override
    @Operation(
            summary = "관리자 로그인",
            description = "성공 시 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)를 Set-Cookie로 내려줍니다."
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String token = adminAuthService.login(request.getUsername(), request.getPassword());
        ResponseCookie cookie = buildAccessTokenCookie(token, jwtService.getExpirationSeconds());

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @Override
    @Operation(
            summary = "관리자 로그아웃",
            description = "토큰 쿠키(ADMIN_ACCESS_TOKEN)를 만료(Max-Age=0)시킵니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = buildAccessTokenCookie("", 0);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @Override
    @Operation(
            summary = "내 정보 조회",
            description = "로그인으로 발급된 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)가 필요합니다."
    )
    @GetMapping("/me")
    public LoginResponse me(@Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal) {
        return LoginResponse.from(principal);
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
