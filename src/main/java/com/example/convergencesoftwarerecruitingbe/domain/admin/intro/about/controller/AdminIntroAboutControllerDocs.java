package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.about.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.about.dto.request.AdminIntroAboutUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AdminIntroAboutControllerDocs {

    @Operation(
            summary = "Intro 텍스트 섹션 수정 (Upsert)",
            description = "소개/하는 일/함께하면 좋은 점 섹션을 업서트합니다. 요청/응답은 GET /api/intro와 동일 구조를 사용합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = IntroResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content)
    })
    ResponseEntity<IntroResponse> updateIntroAbout(
            @Valid
            @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdminIntroAboutUpdateRequest.class),
                            examples = @ExampleObject(value = """
{
  "about": { "title": "비대위란?", "content": "학과 학생회를 지원합니다." },
  "works": { "title": "하는 일", "content": "- 정기회의\\n- 행사기획" },
  "benefits": { "title": "함께하면 좋은 점", "content": "1. 협업 경험\\n2. 네트워킹" }
}
""")
                    )
            ) AdminIntroAboutUpdateRequest request
    );
}
