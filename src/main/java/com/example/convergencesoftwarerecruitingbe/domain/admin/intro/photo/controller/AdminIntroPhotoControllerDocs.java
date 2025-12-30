package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.CreatePhotoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.PresignRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PhotoResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PresignResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AdminIntroPhotoControllerDocs {

    @Operation(
            summary = "Intro 활동사진 업로드용 Presigned URL 발급",
            description = """
                    intro/photos/{uuid}{ext} 형태의 objectKey를 생성하여 presigned PUT URL을 발급합니다.
                    uploadUrl로 PUT 업로드 시 Content-Type 헤더를 반드시 포함해야 합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "presigned URL 발급 성공", content = @Content(schema = @Schema(implementation = PresignResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 (확장자/콘텐츠 타입 불일치 등)", content = @Content),
            @ApiResponse(responseCode = "500", description = "presigned URL 생성 실패", content = @Content)
    })
    ResponseEntity<PresignResponse> presign(
            @Valid
            @RequestBody @Schema(implementation = PresignRequest.class) PresignRequest request
    );

    @Operation(
        summary = "Intro 활동사진 등록 (메타데이터 저장)",
        description = "Presign+PUT 업로드 완료 후 objectKey와 sortOrder를 저장합니다. publicUrl은 서버가 계산하며 201을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = PhotoResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 objectKey/확장자", content = @Content)
    })
    ResponseEntity<PhotoResponse> create(
            @Valid
            @RequestBody @Schema(implementation = CreatePhotoRequest.class) CreatePhotoRequest request
    );

    @Operation(summary = "Intro 활동사진 삭제", description = "해당 사진의 DB 레코드와 MinIO 객체를 함께 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 photoId", content = @Content)
    })
    ResponseEntity<Void> delete(
            @PathVariable Long photoId
    );

    @Operation(
            summary = "Intro 활동사진 목록 조회",
            description = "sortOrder 오름차순, id 오름차순으로 정렬된 목록을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PhotoResponse.class))))
    })
    ResponseEntity<List<PhotoResponse>> list();
}
