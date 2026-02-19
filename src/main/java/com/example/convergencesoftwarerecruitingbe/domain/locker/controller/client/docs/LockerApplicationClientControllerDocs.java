package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Locker Application Client", description = "사물함 신청 API (공개)")
public interface LockerApplicationClientControllerDocs {

    @Operation(summary = "사물함 신청", description = "사물함을 신청합니다. 신청 기간 내에만 가능하며, 동일 학번/전화번호/이메일 중복 체크를 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청 성공"),
            @ApiResponse(responseCode = "400", description = "신청 기간이 아니거나 유효성 검증 실패", content = @Content),
            @ApiResponse(responseCode = "409", description = "이미 예약된 사물함 (낙관적 락 충돌)", content = @Content)
    })
    ResponseEntity<ApplicationResponse> createApplication(@RequestBody ApplicationCreateRequest request);
}
