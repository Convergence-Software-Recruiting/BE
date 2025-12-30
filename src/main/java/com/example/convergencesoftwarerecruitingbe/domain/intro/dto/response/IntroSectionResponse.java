package com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record IntroSectionResponse(
        @Schema(description = "섹션 제목", example = "비대위란?")
        String title,
        @Schema(description = "섹션 본문(줄바꿈/리스트 포함)", example = "- 정기 회의 운영\\n- 행사 기획")
        String content
) {

    public IntroSectionResponse {
        title = title == null ? "" : title;
        content = content == null ? "" : content;
    }

    public static IntroSectionResponse of(String title, String content) {
        return new IntroSectionResponse(title, content);
    }
}
