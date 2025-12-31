package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminFormResultOpenRequest {

    @NotNull
    @Schema(description = "결과 공개 여부", example = "true")
    private Boolean resultOpen;
}
