package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity.IntroPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PhotoResponse {

    @Schema(description = "사진 ID", example = "1")
    private final Long id;
    @Schema(description = "MinIO object key", example = "intro/photos/uuid1.jpg")
    private final String objectKey;
    @Schema(description = "공개 URL", example = "https://s3.bluerack.org/test/intro/photos/uuid1.jpg")
    private final String publicUrl;
    @Schema(description = "정렬 순서", example = "0")
    private final Integer sortOrder;
    @Schema(description = "등록 시각", example = "2025-01-01T10:00:00")
    private final LocalDateTime createdAt;

    public static PhotoResponse from(IntroPhoto photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .objectKey(photo.getObjectKey())
                .publicUrl(photo.getPublicUrl())
                .sortOrder(photo.getSortOrder())
                .createdAt(photo.getCreatedAt())
                .build();
    }
}
