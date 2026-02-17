package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalHistoryResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.RentalStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Locker Rental Admin", description = "대여 관리 API")
public interface LockerRentalAdminControllerDocs {

    @Operation(summary = "활성 대여 목록", description = "반납되지 않은 대여 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<RentalResponse>> getActiveRentals();

    @Operation(summary = "만료 임박 대여 조회", description = "7일 이내 만료되는 대여 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<RentalResponse>> getExpiringRentals();

    @Operation(summary = "수동 반납 처리", description = "대여를 수동으로 반납 처리합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "반납 성공"),
            @ApiResponse(responseCode = "400", description = "이미 반납된 대여", content = @Content),
            @ApiResponse(responseCode = "404", description = "대여를 찾을 수 없음", content = @Content)
    })
    ResponseEntity<RentalResponse> returnRental(
            @Parameter(description = "대여 ID", example = "1") @PathVariable Long id,
            @Parameter(description = "반납 사유", example = "ADMIN_FORCE") @RequestParam ReturnReason reason
    );

    @Operation(summary = "학기 종료 처리", description = "모든 활성 대여를 자동 반납하고 사물함을 CLOSED 처리합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "학기 종료 처리 성공")
    })
    ResponseEntity<Map<String, Integer>> closeSemester();

    @Operation(
            summary = "대여 이력 조회",
            description = "기간 겹침(Overlap) 기준으로 대여 이력을 조회합니다. "
                    + "from/to는 대여 기간과 겹치는 건을 필터합니다 "
                    + "(r.rentalStartDate <= to AND r.rentalEndDate >= from). "
                    + "from/to 중 하나만 전달하면 한쪽 조건만 적용됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<Page<RentalHistoryResponse>> getRentalHistory(
            @Parameter(description = "기간 시작 (yyyy-MM-dd)", example = "2025-03-01") @RequestParam(required = false) LocalDate from,
            @Parameter(description = "기간 종료 (yyyy-MM-dd)", example = "2025-06-30") @RequestParam(required = false) LocalDate to,
            @Parameter(description = "사물함 ID", example = "10") @RequestParam(required = false) Long lockerId,
            @Parameter(description = "학번 해시 (SHA-256)") @RequestParam(required = false) String studentIdHash,
            @Parameter(description = "상태 필터 (ACTIVE: 대여 중, RETURNED: 반납 완료)") @RequestParam(required = false) RentalStatus status,
            @Parameter(hidden = true) Pageable pageable
    );
}
