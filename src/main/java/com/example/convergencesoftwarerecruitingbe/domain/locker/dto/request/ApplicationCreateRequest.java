package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사물함 신청 요청")
public class ApplicationCreateRequest {

    @NotNull
    @Schema(description = "사물함 ID", example = "5")
    private Long lockerId;

    @NotBlank
    @NotNull
    @Schema(description = "신청자 이름", example = "홍길동")
    private String applicantName;

    @NotBlank
    @NotNull
    @Schema(description = "학번", example = "20240001")
    private String studentId;

    @NotBlank
    @NotNull
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @NotBlank
    @NotNull
    @Schema(description = "이메일", example = "hong@example.com")
    private String email;

    @NotNull
    @Schema(description = "학년", example = "GRADE_3")
    private Grade grade;

    @NotNull
    @Schema(description = "전공", example = "CONVERGENCE_SOFTWARE")
    private Major major;
}
