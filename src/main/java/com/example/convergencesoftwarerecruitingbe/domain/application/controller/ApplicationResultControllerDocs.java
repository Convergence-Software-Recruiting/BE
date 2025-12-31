package com.example.convergencesoftwarerecruitingbe.domain.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationResultQueryRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApplicationResultControllerDocs {

    @Operation(
            summary = "지원 결과 조회",
            description = """
지원자는 제출 시 발급받은 4자리 결과 코드로 합격 여부(PASS/FAIL)만 조회할 수 있습니다.
결과 조회는 관리자가 결과 공개를 활성화한 이후에만 가능합니다.
"""
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "403", description = "결과 조회가 아직 열리지 않음", content = @Content),
            @ApiResponse(responseCode = "404", description = "결과 코드를 찾을 수 없음", content = @Content),
            @ApiResponse(responseCode = "409", description = "결과가 확정되지 않음", content = @Content)
    })
    ResponseEntity<ApplicationResultResponse> queryResult(
            @Valid
            @RequestBody
            @Schema(implementation = ApplicationResultQueryRequest.class, example = "{\"resultCode\":\"A7K3\"}")
            ApplicationResultQueryRequest request
    );
}
