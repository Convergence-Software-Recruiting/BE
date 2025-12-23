package com.example.convergencesoftwarerecruitingbe.domain.admin.account.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.ChangePasswordRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.ChangeUsernameRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request.VerifyPasswordRequest;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminAccountControllerDocs {

    @Operation(
            summary = "현재 비밀번호 확인",
            description = """
                    현재 로그인된 관리자 계정의 비밀번호가 맞는지 확인합니다.
                    UX 개선을 위한 API이며, 계정 정보는 변경되지 않습니다.

                    요청 예시(복붙용):
                    {
                      "currentPassword": "admin1234"
                    }
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "비밀번호 일치"),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치 또는 관리자 없음")
    })
    ResponseEntity<Void> verifyPassword(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody VerifyPasswordRequest request
    );

    @Operation(
            summary = "비밀번호 변경",
            description = """
                    현재 비밀번호 검증 후 새 비밀번호로 변경합니다.

                    요청 예시(복붙용):
                    {
                      "currentPassword": "admin1234",
                      "newPassword": "newPassword1234"
                    }
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "401", description = "자격 증명 오류 또는 관리자 없음")
    })
    ResponseEntity<Void> changePassword(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    );

    @Operation(
            summary = "아이디(username) 변경",
            description = """
                    현재 비밀번호를 확인하고 새 username으로 변경 후 JWT 쿠키를 재발급합니다.

                    요청 예시(복붙용):
                    {
                      "newUsername": "admin2",
                      "currentPassword": "admin1234"
                    }
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "아이디 변경 성공 및 쿠키 재발급"),
            @ApiResponse(responseCode = "401", description = "자격 증명 오류 또는 관리자 없음"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 username")
    })
    ResponseEntity<Void> changeUsername(
            @Parameter(hidden = true) @AuthenticationPrincipal AdminPrincipal principal,
            @Valid @RequestBody ChangeUsernameRequest request
    );
}