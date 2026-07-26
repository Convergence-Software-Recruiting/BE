package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사물함 신청 응답")
public class ApplicationResponse {

    @Schema(description = "신청 ID", example = "1")
    private Long id;

    @Schema(description = "사물함 번호", example = "12")
    private Integer lockerNumber;

    @Schema(description = "신청자 이름", example = "홍길동")
    private String applicantName;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @Schema(description = "학년", example = "GRADE_3")
    private Grade grade;

    @Schema(description = "전공", example = "CONVERGENCE_SOFTWARE")
    private Major major;

    @Schema(description = "신청 상태", example = "SUBMITTED")
    private ApplicationStatus status;

    @Schema(description = "신청 일시", example = "2025-03-10T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "결정 일시", example = "2025-03-11T09:00:00")
    private LocalDateTime decidedAt;

    @Schema(description = "결정 사유 (거절 시)", example = "보증금 미납")
    private String decisionReason;

    @Schema(description = "신청 비밀번호 (3자리 숫자)", example = "123")
    private String lockerPassword;

    public static ApplicationResponse from(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .lockerNumber(application.getLocker().getNumber())
                .applicantName(application.getApplicantName())
                .phone(application.getPhone())
                .email(application.getEmail())
                .grade(application.getGrade())
                .major(application.getMajor())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .decidedAt(application.getDecidedAt())
                .decisionReason(application.getDecisionReason())
                .lockerPassword(application.getLockerPassword())
                .build();
    }
}
