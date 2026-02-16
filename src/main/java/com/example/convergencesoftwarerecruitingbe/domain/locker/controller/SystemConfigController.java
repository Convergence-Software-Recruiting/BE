package com.example.convergencesoftwarerecruitingbe.domain.locker.controller;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.SystemConfigUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/locker/admin/config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping
    public ResponseEntity<SystemConfigResponse> getCurrentConfig() {
        return ResponseEntity.ok(systemConfigService.getCurrentConfig());
    }

    @PostMapping
    public ResponseEntity<SystemConfigResponse> createConfig(@Valid @RequestBody SystemConfigUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(systemConfigService.createConfig(request));
    }

    @PostMapping("/application/open")
    public ResponseEntity<SystemConfigResponse> openApplication(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(systemConfigService.openApplication(startDate, endDate));
    }

    @PostMapping("/application/close")
    public ResponseEntity<SystemConfigResponse> closeApplication() {
        return ResponseEntity.ok(systemConfigService.closeApplication());
    }
}
