package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신청 결정(거절) 요청")
public class ApplicationDecisionRequest {

    @NotBlank
    @Schema(description = "거절 사유", example = "보증금 미납")
    private String decisionReason;
}
