package com.example.convergencesoftwarerecruitingbe.domain.locker.enums;

import lombok.Getter;

@Getter
public enum EmailType {
    APPLIED_USER("신청 완료 사용자 안내"),
    APPLIED_ADMIN("신청 접수 관리자 안내"),
    APPROVED_USER("승인 결과 사용자 안내"),
    REJECTED_USER("반려 결과 사용자 안내");

    private final String description;

    private EmailType(String description) {
        this.description = description;
    }
}
