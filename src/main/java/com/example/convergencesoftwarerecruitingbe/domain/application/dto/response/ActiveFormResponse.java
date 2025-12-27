package com.example.convergencesoftwarerecruitingbe.domain.application.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ActiveFormResponse {

    @Schema(description = "폼 ID", example = "1")
    private final Long formId;

    @Schema(description = "제목", example = "2026-1학기 모집")
    private final String title;

    @Schema(description = "설명", example = "설명...")
    private final String description;

    @Schema(description = "질문 리스트", example = """
[
  { "id": 10, "orderNo": 1, "label": "지원 동기", "description": null, "required": true },
  { "id": 11, "orderNo": 2, "label": "경험", "description": "프로젝트 포함", "required": false }
]
""")
    private final List<QuestionItem> questions;

    public static ActiveFormResponse of(Form form, List<Question> questions) {
        List<QuestionItem> items = questions.stream()
                .sorted((a, b) -> Integer.compare(a.getOrderNo(), b.getOrderNo()))
                .map(QuestionItem::from)
                .toList();

        return ActiveFormResponse.builder()
                .formId(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .questions(items)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionItem {

        @Schema(example = "10")
        private final Long id;

        @Schema(example = "1")
        private final Integer orderNo;

        @Schema(example = "지원 동기")
        private final String label;

        @Schema(example = "프로젝트 포함")
        private final String description;

        @Schema(example = "true")
        private final boolean required;

        public static QuestionItem from(Question question) {
            return QuestionItem.builder()
                    .id(question.getId())
                    .orderNo(question.getOrderNo())
                    .label(question.getLabel())
                    .description(question.getDescription())
                    .required(question.isRequired())
                    .build();
        }
    }
}
