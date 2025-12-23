package com.example.convergencesoftwarerecruitingbe.domain.application.dto;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Grade;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Major;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(
        description = "지원서 제출 요청",
        example = """
        {
          "name": "홍길동",
          "studentNo": "20241234",
          "major": "CONVERGENCE_SOFTWARE",
          "grade": "GRADE_1",
          "phone": "010-1234-5678",
          "email": "hong@example.com",
          "answers": [
            { "questionId": 1, "value": "지원 동기입니다..." },
            { "questionId": 2, "value": "자기소개입니다..." },
            { "questionId": 3, "value": "하고 싶은 역할은..." }
          ]
        }
        """
)
public class ApplicationSubmitRequest {

    @Schema(description = "이름", example = "홍길동")
    @NotBlank
    private String name;

    @Schema(description = "학번(중복 제출 방지 키)", example = "20241234")
    @NotBlank
    private String studentNo;

    @Schema(
            description = "전공",
            example = "CONVERGENCE_SOFTWARE",
            allowableValues = {
                    "CONVERGENCE_SOFTWARE",
                    "APPLIED_SOFTWARE",
                    "DATA_SCIENCE",
                    "ARTIFICIAL_INTELLIGENCE"
            }
    )
    @NotNull
    private Major major;

    @Schema(
            description = "학년",
            example = "GRADE_1",
            allowableValues = {"GRADE_1", "GRADE_2", "GRADE_3", "GRADE_4"}
    )
    @NotNull
    private Grade grade;

    @Schema(description = "연락처", example = "010-1234-5678")
    @NotBlank
    private String phone;

    @Schema(description = "이메일", example = "hong@example.com")
    @Email
    @NotBlank
    private String email;

    @ArraySchema(
            arraySchema = @Schema(description = "질문 답변 목록(질문 ID 기반)"),
            schema = @Schema(implementation = AnswerItem.class)
    )
    @Valid
    @NotEmpty
    private List<AnswerItem> answers;

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(description = "질문 답변 항목")
    public static class AnswerItem {

        @Schema(description = "질문 ID", example = "1")
        @NotNull
        private Long questionId;

        @Schema(description = "답변 내용", example = "지원 동기입니다...")
        @NotBlank
        private String value;
    }
}