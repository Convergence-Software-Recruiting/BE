package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SystemConfigResponse {

    private Long id;
    private Boolean applicationOpen;
    private LocalDate applicationStartDate;
    private LocalDate applicationEndDate;
    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;
    private LocalDate rentalEndDate;
    private String depositAccount;
    private Integer depositAmount;
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
