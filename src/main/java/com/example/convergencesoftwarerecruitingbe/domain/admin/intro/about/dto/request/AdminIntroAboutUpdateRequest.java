package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.about.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminIntroAboutUpdateRequest {

    @Valid
    @NotNull
    private SectionRequest about;

    @Valid
    @NotNull
    private SectionRequest works;

    @Valid
    @NotNull
    private SectionRequest benefits;

    @Getter
    @NoArgsConstructor
    public static class SectionRequest {
        @Schema(example = "비대위란?")
        private String title;

        @Schema(example = "- 정기회의\\n- 행사기획")
        private String content;
    }
}
