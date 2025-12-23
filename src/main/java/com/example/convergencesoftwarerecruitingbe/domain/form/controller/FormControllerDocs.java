package com.example.convergencesoftwarerecruitingbe.domain.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitResponse;
import com.example.convergencesoftwarerecruitingbe.domain.form.dto.FormActiveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FormControllerDocs {

    @Operation(summary = "모집 폼 조회", description = "현재 활성화된 폼의 최신 PUBLISHED 버전과 질문을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활성 폼 반환"),
            @ApiResponse(responseCode = "404", description = "활성 폼이 없음", content = @Content)
    })
    ResponseEntity<FormActiveResponse> getActiveForm();

    @Operation(summary = "지원서 제출", description = "폼의 최신 PUBLISHED 버전으로 지원서를 제출합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "지원서 제출 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "폼 또는 버전을 찾을 수 없음", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 제출된 학번", content = @Content)
    })
    ResponseEntity<ApplicationSubmitResponse> submitApplication(
            @PathVariable Long formId,
            @Valid
            @RequestBody @Schema(implementation = ApplicationSubmitRequest.class) ApplicationSubmitRequest request
    );
}
