package com.example.convergencesoftwarerecruitingbe.domain.application.dto.request;

import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ApplicationSubmitRequest {

    @NotBlank
    @Schema(example = "홍길동")
    private String name;

    @NotBlank
    @Schema(example = "202312345")
    private String studentNo;

    @NotNull
    @Schema(example = "CONVERGENCE_SOFTWARE")
    private Major major;

    @NotNull
    @Schema(example = "GRADE_3")
    private Grade grade;

    @NotBlank
    @Schema(example = "010-1234-5678")
    private String phone;

    @NotEmpty
    @Valid
    @Schema(
            description = "질문 답변 목록",
            example = """
    [
      { "questionId": 10, "value": "지원 동기입니다" },
      { "questionId": 11, "value": "경험과 프로젝트" }
    ]
    """
    )
    private List<AnswerItem> answers;

    @Getter
    @NoArgsConstructor
    public static class AnswerItem {

        @NotNull
        @Schema(example = "10")
        private Long questionId;

        @NotBlank
        @Schema(example = "답변 내용")
        private String value;
    }
}
