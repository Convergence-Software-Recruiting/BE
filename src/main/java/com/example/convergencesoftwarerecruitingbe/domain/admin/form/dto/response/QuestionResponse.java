package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuestionResponse {

    private final Long id;
    private final Integer orderNo;
    private final String label;
    private final String description;
    private final boolean required;
    private final LocalDateTime createdAt;

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .orderNo(question.getOrderNo())
                .label(question.getLabel())
                .description(question.getDescription())
                .required(question.isRequired())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
