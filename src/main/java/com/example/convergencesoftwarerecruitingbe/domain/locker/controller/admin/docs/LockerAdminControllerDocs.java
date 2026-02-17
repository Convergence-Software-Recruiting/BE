package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Locker Admin", description = "사물함 관리 API")
public interface LockerAdminControllerDocs {

    @Operation(summary = "전체 사물함 조회", description = "모든 사물함의 현황을 행/열 순서로 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<LockerResponse>> getAllLockers();

    @Operation(summary = "사물함 상태 변경", description = "사물함의 상태를 변경합니다 (EMPTY, CLOSED, BROKEN). " +
            "active rental이 있으면 자동 강제 반납되며, pending application이 있으면 자동 거절됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상태 전환", content = @Content),
            @ApiResponse(responseCode = "404", description = "사물함을 찾을 수 없음", content = @Content)
    })
    ResponseEntity<LockerResponse> updateLockerStatus(
            @Parameter(description = "사물함 ID", example = "5") @PathVariable Long id,
            @Parameter(description = "변경할 상태", example = "BROKEN") @RequestParam LockerStatus status,
            @Parameter(description = "관리자 메모 (BROKEN일 때 필수)", example = "잠금장치 파손") @RequestParam(required = false) String adminNote
    );

    @Operation(summary = "사물함 통계 조회", description = "상태별 사물함 개수를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<Map<String, Long>> getStatistics();

    @Operation(summary = "사물함 일괄 오픈", description = "CLOSED 상태의 일반 사물함(isFixed=false)을 모두 EMPTY로 변경합니다. BROKEN, IN_USE, RESERVED 상태는 변경하지 않습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일괄 오픈 성공")
    })
    ResponseEntity<Map<String, Integer>> openAllLockers();
}
