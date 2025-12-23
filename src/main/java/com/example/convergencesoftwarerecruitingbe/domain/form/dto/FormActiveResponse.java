package com.example.convergencesoftwarerecruitingbe.domain.form.dto;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import com.example.convergencesoftwarerecruitingbe.domain.form.entity.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FormActiveResponse {
    private Long formId;
    private String title;
    private String description;
    private Long versionId;
    private int version;
    private List<QuestionDto> questions;

    @Getter
    @Builder
    public static class QuestionDto {
        private Long id;
        private int orderNo;
        private String label;
        private String description;
        private QuestionType type;
        private boolean required;

        public static QuestionDto from(Question question) {
            return QuestionDto.builder()
                    .id(question.getId())
                    .orderNo(question.getOrderNo())
                    .label(question.getLabel())
                    .description(question.getDescription())
                    .type(question.getType())
                    .required(question.isRequired())
                    .build();
        }
    }

    public static FormActiveResponse of(Form form, FormVersion version, List<Question> questions) {
        List<QuestionDto> questionDtos = questions.stream()
                .map(QuestionDto::from)
                .toList();

        return FormActiveResponse.builder()
                .formId(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .versionId(version.getId())
                .version(version.getVersion())
                .questions(questionDtos)
                .build();
    }
}
