package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Locker Client", description = "사물함 조회 API (공개)")
public interface LockerClientControllerDocs {

    @Operation(summary = "전체 사물함 현황 조회", description = "모든 사물함의 현황을 조회합니다. IN_USE 상태의 사물함에는 마스킹된 대여자 요약 정보가 포함됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<LockerResponse>> getAllLockers();

    @Operation(summary = "신청 가능 사물함 조회", description = "EMPTY 상태이고 고정되지 않은 사물함 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<LockerResponse>> getAvailableLockers();

    @Operation(summary = "현재 학기 설정 조회", description = "신청 기간, 보증금 정보 등 현재 학기 설정을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "시스템 설정이 없음", content = @Content)
    })
    ResponseEntity<SystemConfigResponse> getCurrentConfig();
}
