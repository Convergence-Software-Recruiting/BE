package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LockerResponse {

    private Long id;
    private Integer number;
    private Integer rowNo;
    private Integer colNo;
    private LockerStatus status;
    private Boolean isFixed;

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
}
