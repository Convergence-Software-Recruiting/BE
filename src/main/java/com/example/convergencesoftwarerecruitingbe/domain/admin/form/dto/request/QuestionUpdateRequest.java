package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionUpdateRequest {

    @NotBlank
    private String label;

    private String description;

    @NotNull
    private Boolean required;
}
