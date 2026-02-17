package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사물함 정보 응답")
public class LockerResponse {

    @Schema(description = "사물함 ID", example = "5")
    private Long id;

    @Schema(description = "사물함 번호", example = "12")
    private Integer number;

    @Schema(description = "행 번호", example = "2")
    private Integer rowNo;

    @Schema(description = "열 번호", example = "5")
    private Integer colNo;

    @Schema(description = "사물함 상태", example = "EMPTY")
    private LockerStatus status;

    @Schema(description = "고정 사물함 여부", example = "false")
    private Boolean isFixed;

    @Schema(description = "활성 대여 요약 (IN_USE 상태일 때만 존재)")
    private ActiveRentalSummaryResponse activeRentalSummary;

    public static LockerResponse from(Locker locker) {
        return LockerResponse.builder()
                .id(locker.getId())
                .number(locker.getNumber())
                .rowNo(locker.getRowNo())
                .colNo(locker.getColNo())
                .status(locker.getStatus())
                .isFixed(locker.getIsFixed())
                .build();
    }

    public static LockerResponse withSummary(Locker locker, ActiveRentalSummaryResponse summary) {
        return LockerResponse.builder()
                .id(locker.getId())
                .number(locker.getNumber())
                .rowNo(locker.getRowNo())
                .colNo(locker.getColNo())
                .status(locker.getStatus())
                .isFixed(locker.getIsFixed())
                .activeRentalSummary(summary)
                .build();
    }
}
