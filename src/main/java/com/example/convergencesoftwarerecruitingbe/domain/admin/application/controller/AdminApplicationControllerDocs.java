package com.example.convergencesoftwarerecruitingbe.domain.admin.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.UpdateApplicationMemoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.UpdateApplicationStatusRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminApplicationControllerDocs {

    @Operation(summary = "지원서 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<AdminApplicationListItemResponse>> list();

    @Operation(summary = "지원서 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<AdminApplicationDetailResponse> getDetail(
            Long applicationId
    );

    @Operation(summary = "지원서 상태 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> updateStatus(
            Long applicationId,
            UpdateApplicationStatusRequest request
    );

    @Operation(summary = "지원서 메모 저장/수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메모 저장 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "지원서를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> updateMemo(
            Long applicationId,
            UpdateApplicationMemoRequest request
    );
}
