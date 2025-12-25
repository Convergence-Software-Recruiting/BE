package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminFormResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final List<QuestionResponse> questions;

    public static AdminFormResponse of(Form form, List<Question> questions) {
        List<QuestionResponse> questionResponses = questions.stream()
                .sorted((q1, q2) -> Integer.compare(q1.getOrderNo(), q2.getOrderNo()))
                .map(QuestionResponse::from)
                .toList();

        return AdminFormResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .active(form.isActive())
                .createdAt(form.getCreatedAt())
                .questions(questionResponses)
                .build();
    }
}
