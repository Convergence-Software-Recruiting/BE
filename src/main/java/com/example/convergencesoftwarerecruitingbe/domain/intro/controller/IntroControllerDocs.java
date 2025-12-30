package com.example.convergencesoftwarerecruitingbe.domain.intro.controller;

import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface IntroControllerDocs {

    @Operation(
            summary = "소개 페이지 전체 조회 (공용)",
            description = "소개/하는 일/함께하면 좋은 점 텍스트와 소개 사진 목록을 함께 조회합니다. works/benefits content는 줄바꿈(예: \"- 정기회의\\n- 행사기획\")을 그대로 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "소개 페이지 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = IntroResponse.class),
                            examples = @ExampleObject(value = """
{
  "about": { "title": "비대위란?", "content": "학과 학생회를 지원합니다." },
  "works": { "title": "하는 일", "content": "- 정기 회의 운영\\n- 행사 기획" },
  "benefits": { "title": "함께하면 좋은 점", "content": "1. 협업 경험\\n2. 네트워킹" },
  "photos": [
    { "id": 1, "objectKey": "intro/photos/uuid1.jpg", "publicUrl": "https://s3.bluerack.org/test/intro/photos/uuid1.jpg", "sortOrder": 0, "createdAt": "2025-01-01T10:00:00" },
    { "id": 2, "objectKey": "intro/photos/uuid2.jpg", "publicUrl": "https://s3.bluerack.org/test/intro/photos/uuid2.jpg", "sortOrder": 1, "createdAt": "2025-01-02T10:00:00" }
  ],
  "updatedAt": "2025-12-30T12:34:56"
}
""")
                    )
            )
    })
    ResponseEntity<IntroResponse> getIntro();
}
