package com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "관리자 로그인 요청")
public class LoginRequest {

    @Schema(example = "admin")
    @NotBlank
    private String username;

    @Schema(example = "admin1234")
    @NotBlank
    private String password;
}