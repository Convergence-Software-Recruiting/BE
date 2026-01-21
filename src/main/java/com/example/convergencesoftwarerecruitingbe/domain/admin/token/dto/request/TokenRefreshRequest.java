package com.example.convergencesoftwarerecruitingbe.domain.admin.token.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "토큰 재발급 요청")
public class TokenRefreshRequest {

    @NotBlank
    @Schema(example = "refresh-token")
    private String refreshToken;
}
