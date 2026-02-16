package com.example.convergencesoftwarerecruitingbe.domain.locker.controller;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.ApplicationService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.LockerService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.SystemConfigService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/locker")
public class LockerPublicController {

    private final LockerService lockerService;
    private final ApplicationService applicationService;
    private final SystemConfigService systemConfigService;

    @GetMapping("/status")
    public ResponseEntity<List<LockerResponse>> getLockerStatus() {
        return ResponseEntity.ok(lockerService.getAllLockers());
    }

    @GetMapping("/available")
    public ResponseEntity<List<LockerResponse>> getAvailableLockers() {
        return ResponseEntity.ok(lockerService.getAvailableLockers());
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationCreateRequest request) {
        if (!systemConfigService.isApplicationOpen()) {
            throw new IllegalStateException("현재 신청 기간이 아닙니다");
        }

        ApplicationResponse response = applicationService.createApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLockException(OptimisticLockException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "이미 예약된 사물함입니다. 다른 사물함을 선택해주세요."));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}
