package com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.intro.entity.IntroContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class IntroResponse {

    @Schema(description = "소개 섹션")
    private final IntroSectionResponse about;
    @Schema(description = "하는 일 섹션 (줄바꿈/리스트 포함)")
    private final IntroSectionResponse works;
    @Schema(description = "함께하면 좋은 점 섹션 (줄바꿈/리스트 포함)")
    private final IntroSectionResponse benefits;
    @Schema(description = "마지막 수정 시각", example = "2025-12-30T12:34:56")
    private final LocalDateTime updatedAt;

    public static IntroResponse from(IntroContent content) {
        return IntroResponse.builder()
                .about(IntroSectionResponse.of(content.getAboutTitle(), content.getAboutContent()))
                .works(IntroSectionResponse.of(content.getWorksTitle(), content.getWorksContent()))
                .benefits(IntroSectionResponse.of(content.getBenefitsTitle(), content.getBenefitsContent()))
                .updatedAt(content.getUpdatedAt())
                .build();
    }

    public static IntroResponse empty() {
        return IntroResponse.builder()
                .about(IntroSectionResponse.of("", ""))
                .works(IntroSectionResponse.of("", ""))
                .benefits(IntroSectionResponse.of("", ""))
                .updatedAt(null)
                .build();
    }
}
