package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "학기 설정 응답")
public class SystemConfigResponse {

    @Schema(description = "설정 ID", example = "1")
    private Long id;

    @Schema(description = "신청 기간 오픈 여부", example = "true")
    private Boolean applicationOpen;

    @Schema(description = "신청 시작일", example = "2026-02-17")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate applicationStartDate;

    @Schema(description = "신청 종료일", example = "2026-05-18")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate applicationEndDate;

    @Schema(description = "학기 시작일", example = "2026-03-02")
    private LocalDate semesterStartDate;

    @Schema(description = "학기 종료일", example = "2026-06-20")
    private LocalDate semesterEndDate;

    @Schema(description = "대여 종료일", example = "2026-06-27")
    private LocalDate rentalEndDate;

    @Schema(description = "보증금 입금 계좌", example = "카카오뱅크 3333-01-1234567")
    private String depositAccount;

    @Schema(description = "보증금 금액 (원)", example = "3000")
    private Integer depositAmount;

    @Schema(description = "보증금 입금 기한 (일)", example = "3")
    private Integer depositDueDays;

    public static SystemConfigResponse from(SystemConfig config) {
        return SystemConfigResponse.builder()
                .id(config.getId())
                .applicationOpen(config.getApplicationOpen())
                .applicationStartDate(config.getApplicationStartDate())
                .applicationEndDate(config.getApplicationEndDate())
                .semesterStartDate(config.getSemesterStartDate())
                .semesterEndDate(config.getSemesterEndDate())
                .rentalEndDate(config.getRentalEndDate())
                .depositAccount(config.getDepositAccount())
                .depositAmount(config.getDepositAmount())
                .depositDueDays(config.getDepositDueDays())
                .build();
    }
}
