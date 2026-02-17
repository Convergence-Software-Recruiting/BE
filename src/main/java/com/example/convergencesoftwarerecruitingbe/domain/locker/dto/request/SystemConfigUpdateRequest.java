package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "학기 설정 생성 요청")
public class SystemConfigUpdateRequest {

    @Schema(description = "학기 시작일", example = "2026-03-02")
    private LocalDate semesterStartDate;

    @NotNull
    @Schema(description = "학기 종료일", example = "2026-06-20")
    private LocalDate semesterEndDate;

    @NotNull
    @Schema(description = "대여 종료일", example = "2026-06-27")
    private LocalDate rentalEndDate;

    @Schema(description = "보증금 입금 계좌", example = "카카오뱅크 3333-01-1234567")
    private String depositAccount;

    @Schema(description = "보증금 금액 (원)", example = "5000")
    private Integer depositAmount;

    @Schema(description = "보증금 입금 기한 (일)", example = "3")
    private Integer depositDueDays;
}
