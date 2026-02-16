package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreateRequest {

    @NotNull
    private Long lockerId;

    @NotBlank
    @NotNull
    private String applicantName;

    @NotBlank
    @NotNull
    private String studentId;

    @NotBlank
    @NotNull
    private String phone;

    @NotBlank
    @NotNull
    private String email;

    @NotNull
    private Grade grade;

    @NotNull
    private Major major;
}
