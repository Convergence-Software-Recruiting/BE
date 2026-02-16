package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Locker Application Client", description = "사물함 신청 API (공개)")
public interface LockerApplicationClientControllerDocs {

    @Operation(summary = "사물함 신청", description = "사물함을 신청합니다 (중복 체크 포함)")
    ResponseEntity<ApplicationResponse> createApplication(@RequestBody ApplicationCreateRequest request);
}
