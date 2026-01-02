package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SyncPhotosRequest {

    @NotEmpty
    @Valid
    @Schema(description = "최종 확정된 사진 목록(배열 순서=sortOrder)", example = """
[
  { "objectKey": "intro/photos/uuid1.jpg", "publicUrl": "https://bucket/intro/photos/uuid1.jpg" },
  { "objectKey": "intro/photos/uuid2.jpg", "publicUrl": "https://bucket/intro/photos/uuid2.jpg" }
]""")
    private List<PhotoItem> photos;

    @Getter
    @NoArgsConstructor
    public static class PhotoItem {

        @Schema(description = "업로드된 객체 키", example = "intro/photos/uuid1.jpg")
        private String objectKey;

        @Schema(description = "공개 URL (objectKey가 없을 때만 사용)", example = "https://bucket/intro/photos/uuid1.jpg")
        private String publicUrl;
    }
}
