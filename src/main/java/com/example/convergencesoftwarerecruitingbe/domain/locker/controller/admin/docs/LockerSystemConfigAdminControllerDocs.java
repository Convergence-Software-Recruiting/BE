package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.SystemConfigUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Locker System Config Admin", description = "학기 설정 관리 API")
public interface LockerSystemConfigAdminControllerDocs {

    @Operation(summary = "현재 학기 설정 조회", description = "최신 학기 설정을 조회합니다")
    ResponseEntity<SystemConfigResponse> getCurrentConfig();

    @Operation(summary = "새 학기 설정 생성", description = "새로운 학기 설정을 생성합니다")
    ResponseEntity<SystemConfigResponse> createConfig(@RequestBody SystemConfigUpdateRequest request);

    @Operation(summary = "신청 기간 오픈", description = "신청 기간을 설정하고 오픈합니다")
    ResponseEntity<SystemConfigResponse> openApplication(
            @Parameter(description = "신청 시작일") @RequestParam LocalDate startDate,
            @Parameter(description = "신청 종료일") @RequestParam LocalDate endDate
    );

    @Operation(summary = "신청 기간 마감", description = "신청을 마감합니다")
    ResponseEntity<SystemConfigResponse> closeApplication();
}
