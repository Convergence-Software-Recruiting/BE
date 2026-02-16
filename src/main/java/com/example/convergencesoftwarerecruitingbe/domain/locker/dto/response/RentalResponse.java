package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RentalResponse {

    private Long id;
    private Integer lockerNumber;
    private String renterName;
    private String renterPhone;
    private String renterEmail;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private LocalDateTime approvedAt;
    private LocalDateTime returnedAt;
    private ReturnReason returnReason;

    public static RentalResponse from(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .lockerNumber(rental.getLocker().getNumber())
                .renterName(rental.getRenterName())
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
