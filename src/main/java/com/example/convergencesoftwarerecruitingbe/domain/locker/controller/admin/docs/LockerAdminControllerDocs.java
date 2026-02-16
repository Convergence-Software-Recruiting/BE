package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Locker Admin", description = "사물함 관리 API")
public interface LockerAdminControllerDocs {

    @Operation(summary = "전체 사물함 조회", description = "모든 사물함의 현황을 행/열 순서로 조회합니다")
    ResponseEntity<List<LockerResponse>> getAllLockers();

    @Operation(summary = "사물함 상태 변경", description = "사물함의 상태를 변경합니다 (EMPTY, CLOSED, BROKEN)")
    ResponseEntity<LockerResponse> updateLockerStatus(
            @Parameter(description = "사물함 ID") @PathVariable Long id,
            @Parameter(description = "변경할 상태") @RequestParam LockerStatus status,
            @Parameter(description = "관리자 메모 (BROKEN일 때 필수)") @RequestParam(required = false) String adminNote
    );

    @Operation(summary = "사물함 통계 조회", description = "상태별 사물함 개수를 조회합니다")
    ResponseEntity<Map<String, Long>> getStatistics();
}
