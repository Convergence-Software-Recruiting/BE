package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "활성 대여 요약 (마스킹 적용)")
public class ActiveRentalSummaryResponse {

    @Schema(description = "마스킹된 이름", example = "김*진")
    private String maskedName;

    @Schema(description = "전화번호 끝 4자리", example = "2633")
    private String phoneLast4;

    @Schema(description = "대여 시작일", example = "2025-03-11")
    private LocalDate rentalStartDate;

    @Schema(description = "대여 종료일", example = "2025-06-13")
    private LocalDate rentalEndDate;

    public static ActiveRentalSummaryResponse from(Rental rental) {
        return ActiveRentalSummaryResponse.builder()
                .maskedName(maskName(rental.getRenterName()))
                .phoneLast4(extractLast4(rental.getRenterPhone()))
                .rentalStartDate(rental.getRentalStartDate())
                .rentalEndDate(rental.getRentalEndDate())
                .build();
    }

    private static String maskName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1);
    }

    private static String extractLast4(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        if (digitsOnly.length() < 4) {
            return digitsOnly;
        }
        return digitsOnly.substring(digitsOnly.length() - 4);
    }
}
