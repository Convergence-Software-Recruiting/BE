package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Grade;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Major;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminApplicationDetailResponse {
    private Long id;
    private String name;
    private String studentNo;
    private Major major;
    private Grade grade;
    private String phone;
    private String email;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;
    private String adminMemo;
    private List<AnswerDto> answers;

    @Getter
    @Builder
    public static class AnswerDto {
        private Long questionId;
        private String questionLabel;
        private String value;

        public static AnswerDto from(ApplicationAnswer answer) {
            Question question = answer.getQuestion();
            return AnswerDto.builder()
                    .questionId(question.getId())
                    .questionLabel(question.getLabel())
                    .value(answer.getValue())
                    .build();
        }
    }

    public static AdminApplicationDetailResponse of(Application application, List<ApplicationAnswer> answers) {
        List<AnswerDto> answerDtos = answers.stream()
                .map(AnswerDto::from)
                .toList();

        return AdminApplicationDetailResponse.builder()
                .id(application.getId())
                .name(application.getName())
                .studentNo(application.getStudentNo())
                .major(application.getMajor())
                .grade(application.getGrade())
                .phone(application.getPhone())
                .email(application.getEmail())
                .status(application.getStatus())
                .submittedAt(application.getSubmittedAt())
                .adminMemo(application.getAdminMemo())
                .answers(answerDtos)
                .build();
    }
}
