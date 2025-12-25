package com.example.convergencesoftwarerecruitingbe.domain.admin.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.AdminFormCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormListItemResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AdminFormControllerDocs {

    @Operation(summary = "모집 폼 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content)
    })
    ResponseEntity<AdminFormListItemResponse> create(
            @Valid
            @RequestBody @Schema(implementation = AdminFormCreateRequest.class) AdminFormCreateRequest request
    );

    @Operation(summary = "모집 폼 목록 조회")
    ResponseEntity<List<AdminFormListItemResponse>> list();

    @Operation(summary = "모집 폼 상세 조회 (질문 포함)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "폼을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<AdminFormResponse> get(
            @PathVariable Long id
    );

    @Operation(summary = "현재 모집으로 설정(다른 폼은 자동 비활성화)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "활성화 성공"),
            @ApiResponse(responseCode = "404", description = "폼을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> activate(
            @PathVariable Long id
    );

    @Operation(summary = "모집 종료(모든 폼 비활성화)")
    @ApiResponse(responseCode = "204", description = "비활성화 성공")
    ResponseEntity<Void> deactivate();
}
