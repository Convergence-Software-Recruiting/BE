package com.example.convergencesoftwarerecruitingbe.domain.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationSubmitResponse {

    @Schema(description = "저장된 지원서 ID", example = "123")
    private final Long applicationId;

    @Schema(description = "지원 결과 조회 코드", example = "A7K9")
    private final String resultCode;
}
