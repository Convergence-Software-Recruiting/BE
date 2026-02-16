package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationDecisionRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    ResponseEntity<Page<ApplicationResponse>> getApplications(
            @Parameter(description = "필터링할 상태 (null이면 전체)") @RequestParam(required = false) ApplicationStatus status,
            Pageable pageable
    );

    @Operation(summary = "미처리 신청 개수", description = "SUBMITTED 상태의 신청서 개수를 조회합니다")
    ResponseEntity<Long> getPendingCount();

    @Operation(summary = "신청 승인", description = "신청을 승인하고 대여를 시작합니다")
    ResponseEntity<RentalResponse> approveApplication(
            @Parameter(description = "신청 ID") @PathVariable Long id,
            Authentication authentication
    );

    @Operation(summary = "신청 거절", description = "신청을 거절하고 사물함을 EMPTY로 복원합니다")
    ResponseEntity<Void> rejectApplication(
            @Parameter(description = "신청 ID") @PathVariable Long id,
            @RequestBody ApplicationDecisionRequest request,
            Authentication authentication
    );
}
