package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationAnswer;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminApplicationDetailResponse {

    private final Long applicationId;
    private final Long formId;
    private final String name;
    private final String studentNo;
    private final Major major;
    private final Grade grade;
    private final String phone;
    private final ApplicationStatus status;
    private final String adminMemo;
    private final LocalDateTime submittedAt;
    private final List<AnswerItem> answers;

    public static AdminApplicationDetailResponse of(Application application, List<ApplicationAnswer> answers) {
        List<AnswerItem> answerItems = answers.stream()
                .sorted(Comparator.comparing(a -> a.getQuestion().getOrderNo()))
                .map(AnswerItem::from)
                .toList();

        return AdminApplicationDetailResponse.builder()
                .applicationId(application.getId())
                .formId(application.getForm().getId())
                .name(application.getName())
                .studentNo(application.getStudentNo())
                .major(application.getMajor())
                .grade(application.getGrade())
                .phone(application.getPhone())
                .status(application.getStatus())
                .adminMemo(application.getAdminMemo())
                .submittedAt(application.getSubmittedAt())
                .answers(answerItems)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AnswerItem {

        private final Long questionId;
        private final Integer orderNo;
        private final String label;
        private final String value;
        private final boolean required;

        public static AnswerItem from(ApplicationAnswer applicationAnswer) {
            Question question = applicationAnswer.getQuestion();
            return AnswerItem.builder()
                    .questionId(question.getId())
                    .orderNo(question.getOrderNo())
                    .label(question.getLabel())
                    .value(applicationAnswer.getValue())
                    .required(question.isRequired())
                    .build();
        }
    }
}
