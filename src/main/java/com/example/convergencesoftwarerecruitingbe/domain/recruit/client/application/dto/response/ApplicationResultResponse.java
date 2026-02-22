package com.example.convergencesoftwarerecruitingbe.domain.recruit.client.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationResultResponse {

    @Schema(description = "지원자 이름", example = "홍길동")
    private final String name;

    @Schema(description = "합격 여부 (PASS 또는 FAIL)", example = "PASS")
    private final String status;
}
