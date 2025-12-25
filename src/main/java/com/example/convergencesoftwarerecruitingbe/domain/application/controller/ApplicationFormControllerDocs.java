package com.example.convergencesoftwarerecruitingbe.domain.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ActiveFormResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationSubmitResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApplicationFormControllerDocs {

    @Operation(summary = "현재 활성화된 모집 폼 조회 (질문 포함)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "활성 폼 반환"),
            @ApiResponse(responseCode = "404", description = "활성 폼이 없음", content = @Content)
    })
    ResponseEntity<ActiveFormResponse> getActiveForm();

    @Operation(summary = "활성 폼에 지원서 제출")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "제출 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "활성 폼이 없음", content = @Content),
            @ApiResponse(responseCode = "409", description = "중복 제출", content = @Content)
    })
    ResponseEntity<ApplicationSubmitResponse> submitActiveApplication(
            @Valid
            @RequestBody
            @Schema(implementation = ApplicationSubmitRequest.class)
            ApplicationSubmitRequest request
    );
}