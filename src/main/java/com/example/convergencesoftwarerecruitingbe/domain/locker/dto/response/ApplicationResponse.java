package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;
    private Integer lockerNumber;
    private String applicantName;
    private String phone;
    private String email;
    private Grade grade;
    private Major major;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;
    private String decisionReason;

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
                .build();
    }
}
