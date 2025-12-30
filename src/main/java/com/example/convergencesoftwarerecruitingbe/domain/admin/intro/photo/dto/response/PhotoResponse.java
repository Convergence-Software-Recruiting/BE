package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity.IntroPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PhotoResponse {

    private final Long id;
    private final String objectKey;
    private final String publicUrl;
    private final Integer sortOrder;
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
