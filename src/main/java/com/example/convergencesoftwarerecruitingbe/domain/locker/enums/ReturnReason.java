package com.example.convergencesoftwarerecruitingbe.domain.locker.enums;

import lombok.Getter;

@Getter
public enum ReturnReason {
    AUTO_END("자동 종료"),
    ADMIN_FORCE("관리자 강제 반납"),
    BROKEN_FORCE("고장으로 인한 강제 반납");

    private final String description;

    private ReturnReason(String description) {
        this.description = description;
    }
}
