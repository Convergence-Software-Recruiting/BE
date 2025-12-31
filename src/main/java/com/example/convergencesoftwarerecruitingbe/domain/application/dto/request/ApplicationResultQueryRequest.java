package com.example.convergencesoftwarerecruitingbe.domain.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationResultQueryRequest {

    @NotBlank
    @Pattern(regexp = "(?i)^[ABCDEFGHJKMNPQRSTUVWXYZ23456789]{4}$")
    @Schema(description = "지원 결과 조회 코드", example = "A7K9")
    private String resultCode;
}
