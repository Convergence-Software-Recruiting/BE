package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminFormResultOpenResponse {

    @Schema(example = "1")
    private final Long formId;

    @Schema(example = "true")
    private final boolean resultOpen;

    public static AdminFormResultOpenResponse from(Form form) {
        return new AdminFormResultOpenResponse(form.getId(), form.isResultOpen());
    }
}
