package com.example.convergencesoftwarerecruitingbe.domain.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResultResponse {

    @Schema(description = "합격 여부 (PASS 또는 FAIL)", example = "PASS")
    private final String status;
}
