package com.example.convergencesoftwarerecruitingbe.domain.locker.enums;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    SUBMITTED("제출됨"),
    APPROVED("승인됨"),
    REJECTED("반려됨");

    private final String description;

    private ApplicationStatus(String description) {
        this.description = description;
    }
}
