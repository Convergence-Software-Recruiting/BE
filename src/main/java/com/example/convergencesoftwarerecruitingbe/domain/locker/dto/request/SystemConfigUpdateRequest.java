package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigUpdateRequest {

    private LocalDate applicationStartDate;

    private LocalDate applicationEndDate;

    private LocalDate semesterStartDate;

    @NotNull
    private LocalDate semesterEndDate;

    @NotNull
    private LocalDate rentalEndDate;

    private String depositAccount;

    private Integer depositAmount;

    private Integer depositDueDays;
}
