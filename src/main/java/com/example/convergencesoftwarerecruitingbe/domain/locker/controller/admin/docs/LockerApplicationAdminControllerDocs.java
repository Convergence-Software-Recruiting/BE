package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationDecisionRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Locker Application Admin", description = "사물함 신청 관리 API")
public interface LockerApplicationAdminControllerDocs {

    @Operation(summary = "신청 목록 조회", description = "상태별 필터링 및 페이징 지원")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<Page<ApplicationResponse>> getApplications(
            @Parameter(description = "필터링할 상태 (null이면 전체)", example = "SUBMITTED") @RequestParam(required = false) ApplicationStatus status,
            Pageable pageable
    );

    @Operation(summary = "미처리 신청 개수", description = "SUBMITTED 상태의 신청서 개수를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<Long> getPendingCount();

    @Operation(summary = "신청 승인", description = "신청을 승인하고 대여를 시작합니다. 신청 기간이 종료되어도 승인 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승인 성공, 대여 생성됨"),
            @ApiResponse(responseCode = "400", description = "이미 처리된 신청", content = @Content),
            @ApiResponse(responseCode = "404", description = "신청을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<RentalResponse> approveApplication(
            @Parameter(description = "신청 ID", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) Authentication authentication
    );

    @Operation(summary = "신청 거절", description = "신청을 거절하고 사물함을 EMPTY로 복원합니다. 신청 기간이 종료되어도 거절 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거절 성공"),
            @ApiResponse(responseCode = "400", description = "이미 처리된 신청", content = @Content),
            @ApiResponse(responseCode = "404", description = "신청을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<Void> rejectApplication(
            @Parameter(description = "신청 ID", example = "1") @PathVariable Long id,
            @RequestBody ApplicationDecisionRequest request,
            @Parameter(hidden = true) Authentication authentication
    );
}
