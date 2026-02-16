package com.example.convergencesoftwarerecruitingbe.domain.locker.enums;

import lombok.Getter;

@Getter
public enum LockerStatus {
    EMPTY("비어있음"),
    RESERVED("예약됨"),
    IN_USE("사용 중"),
    BROKEN("고장"),
    CLOSED("폐쇄");

    private final String description;

    private LockerStatus(String description) {
        this.description = description;
    }
}
