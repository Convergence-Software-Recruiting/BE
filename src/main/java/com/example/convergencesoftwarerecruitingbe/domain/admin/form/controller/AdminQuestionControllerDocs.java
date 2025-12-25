package com.example.convergencesoftwarerecruitingbe.domain.admin.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.QuestionUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface AdminQuestionControllerDocs {

    @Operation(summary = "질문 생성 (orderNo는 서버에서 자동 할당)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "폼을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long formId,
            @Valid @RequestBody @Schema(implementation = QuestionCreateRequest.class) QuestionCreateRequest request
    );

    @Operation(summary = "질문 내용 수정(순서 제외)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청", content = @Content),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long questionId,
            @Valid @RequestBody @Schema(implementation = QuestionUpdateRequest.class) QuestionUpdateRequest request
    );

    @Operation(summary = "질문 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId
    );
}
