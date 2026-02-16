package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs.LockerApplicationClientControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.ApplicationService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.SystemConfigService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/client/locker/applications")
@RequiredArgsConstructor
public class LockerApplicationClientController implements LockerApplicationClientControllerDocs {

    private final ApplicationService applicationService;
    private final SystemConfigService systemConfigService;

    @Override
    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @Valid @RequestBody ApplicationCreateRequest request
    ) {
        if (!systemConfigService.isApplicationOpen()) {
            throw new IllegalStateException("신청 기간이 아닙니다");
        }

        ApplicationResponse response = applicationService.createApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLockException(OptimisticLockException e) {
        log.warn("낙관적 락 충돌 발생", e);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "이미 예약된 사물함입니다. 다른 사물함을 선택해주세요."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        log.warn("비즈니스 규칙 위반", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
