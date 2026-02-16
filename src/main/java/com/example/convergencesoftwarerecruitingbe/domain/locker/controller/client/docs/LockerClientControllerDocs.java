package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Locker Client", description = "사물함 조회 API (공개)")
public interface LockerClientControllerDocs {

    @Operation(summary = "전체 사물함 현황 조회", description = "모든 사물함의 현황을 조회합니다")
    ResponseEntity<List<LockerResponse>> getAllLockers();

    @Operation(summary = "신청 가능 사물함 조회", description = "EMPTY 상태이고 고정되지 않은 사물함 목록을 조회합니다")
    ResponseEntity<List<LockerResponse>> getAvailableLockers();
}
