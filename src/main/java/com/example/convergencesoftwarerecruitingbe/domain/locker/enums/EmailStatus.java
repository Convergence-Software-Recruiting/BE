package com.example.convergencesoftwarerecruitingbe.domain.locker.enums;

import lombok.Getter;

@Getter
public enum EmailStatus {
    SENT("발송 성공"),
    FAILED("발송 실패");

    private final String description;

    private EmailStatus(String description) {
        this.description = description;
    }
}
