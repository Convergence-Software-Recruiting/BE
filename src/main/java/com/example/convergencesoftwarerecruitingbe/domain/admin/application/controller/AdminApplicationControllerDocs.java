package com.example.convergencesoftwarerecruitingbe.domain.admin.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.AdminApplicationMemoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.AdminApplicationStatusRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminApplicationControllerDocs {

    @Operation(summary = "지원서 목록 조회", description = "상태별로 지원서 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "목록 조회 성공")
    })
    ResponseEntity<Page<AdminApplicationListResponse>> list(
            @RequestParam(required = false) ApplicationStatus status,

            // Pageable 대신 Swagger용 query params 명시
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "정렬. 예) submittedAt,desc 또는 id,asc", example = "submittedAt,desc")
            @RequestParam(required = false) String sort
    );

    @Operation(summary = "지원서 상세 조회", description = "고정 필드 및 질문/답변을 포함해 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<AdminApplicationDetailResponse> getDetail(@PathVariable Long id);

    @Operation(summary = "지원서 상태 변경", description = "지원서 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AdminApplicationStatusRequest request
    );

    @Operation(summary = "지원서 메모 수정", description = "관리자 메모를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "메모 수정 성공"),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> updateMemo(
            @PathVariable Long id,
            @Valid @RequestBody AdminApplicationMemoRequest request
    );
}