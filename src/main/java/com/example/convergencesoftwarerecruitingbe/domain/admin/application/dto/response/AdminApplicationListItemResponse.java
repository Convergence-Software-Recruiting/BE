package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminApplicationListItemResponse {

    private final Long applicationId;
    private final String name;
    private final String studentNo;
    private final Major major;
    private final Grade grade;
    private final ApplicationStatus status;
    private final LocalDateTime submittedAt;

    public static AdminApplicationListItemResponse from(Application application) {
        return AdminApplicationListItemResponse.builder()
                .applicationId(application.getId())
                .name(application.getName())
                .studentNo(application.getStudentNo())
                .major(application.getMajor())
                .grade(application.getGrade())
                .status(application.getStatus())
                .submittedAt(application.getSubmittedAt())
                .build();
    }
}
