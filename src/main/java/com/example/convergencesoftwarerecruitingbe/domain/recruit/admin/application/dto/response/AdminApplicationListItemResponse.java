package com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.recruit.client.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Department;
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
    private final Department firstChoice;
    private final ApplicationStatus status;
    private final LocalDateTime submittedAt;

    public static AdminApplicationListItemResponse from(Application application) {
        return AdminApplicationListItemResponse.builder()
                .applicationId(application.getId())
                .name(application.getName())
                .studentNo(application.getStudentNo())
                .major(application.getMajor())
                .grade(application.getGrade())
                .firstChoice(application.getFirstChoice())
                .status(application.getStatus())
                .submittedAt(application.getSubmittedAt())
                .build();
    }
}
