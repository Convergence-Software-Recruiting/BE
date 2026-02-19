package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "대여 이력 응답")
public class RentalHistoryResponse {

    @Schema(description = "대여 ID", example = "1")
    private Long rentalId;

    @Schema(description = "사물함 ID", example = "10")
    private Long lockerId;

    @Schema(description = "사물함 번호", example = "12")
    private Integer lockerNumber;

    @Schema(description = "학번", example = "20240001")
    private String studentId;

    @Schema(description = "신청자 이름", example = "홍길동")
    private String applicantName;

    @Schema(description = "대여자 전화번호", example = "010-1234-5678")
    private String renterPhone;

    @Schema(description = "대여자 이메일", example = "hong@example.com")
    private String renterEmail;

    @Schema(description = "대여 시작일", example = "2025-03-11")
    private LocalDate rentalStartDate;

    @Schema(description = "대여 종료일", example = "2025-06-13")
    private LocalDate rentalEndDate;

    @Schema(description = "승인 일시", example = "2025-03-11T09:00:00")
    private LocalDateTime approvedAt;

    @Schema(description = "반납 일시 (null이면 대여 중)", example = "2025-06-13T18:00:00")
    private LocalDateTime returnedAt;

    @Schema(description = "반납 사유", example = "AUTO_END")
    private ReturnReason returnReason;

    public static RentalHistoryResponse from(Rental rental) {
        return RentalHistoryResponse.builder()
                .rentalId(rental.getId())
                .lockerId(rental.getLockerId())
                .lockerNumber(rental.getLocker().getNumber())
                .studentId(rental.getApplication().getStudentId())
                .applicantName(rental.getApplication().getApplicantName())
                .renterPhone(rental.getRenterPhone())
                .renterEmail(rental.getRenterEmail())
                .rentalStartDate(rental.getRentalStartDate())
                .rentalEndDate(rental.getRentalEndDate())
                .approvedAt(rental.getApprovedAt())
                .returnedAt(rental.getReturnedAt())
                .returnReason(rental.getReturnReason())
                .build();
    }
}
