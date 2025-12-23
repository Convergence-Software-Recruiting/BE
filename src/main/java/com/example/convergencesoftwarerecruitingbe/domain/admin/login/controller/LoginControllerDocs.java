package com.example.convergencesoftwarerecruitingbe.domain.admin.login.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.request.LoginRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.response.LoginResponse;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginControllerDocs {

    @Operation(
            summary = "관리자 로그인",
            description = """
                    username/password로 로그인합니다.
                    성공 시 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)를 Set-Cookie로 내려줍니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그인 성공 (Set-Cookie로 토큰 쿠키 발급)"),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content)
    })
    ResponseEntity<Void> login(
            @RequestBody @Schema(implementation = LoginRequest.class) LoginRequest request
    );

    @Operation(
            summary = "관리자 로그아웃",
            description = "토큰 쿠키(ADMIN_ACCESS_TOKEN)를 만료(Max-Age=0)시킵니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공 (Set-Cookie로 쿠키 만료)")
    })
    ResponseEntity<Void> logout();

    @Operation(
            summary = "내 정보 조회",
            description = "로그인으로 발급된 HttpOnly 쿠키(ADMIN_ACCESS_TOKEN)가 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content)
    })
    LoginResponse me(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal
    );
}