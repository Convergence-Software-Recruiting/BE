package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Grade;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Major;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminApplicationListResponse {
    private Long id;
    private String name;
    private String studentNo;
    private Major major;
    private Grade grade;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;

    public static AdminApplicationListResponse from(Application application) {
        return AdminApplicationListResponse.builder()
                .id(application.getId())
                .name(application.getName())
                .studentNo(application.getStudentNo())
                .major(application.getMajor())
                .grade(application.getGrade())
                .status(application.getStatus())
                .submittedAt(application.getSubmittedAt())
                .build();
    }
}
