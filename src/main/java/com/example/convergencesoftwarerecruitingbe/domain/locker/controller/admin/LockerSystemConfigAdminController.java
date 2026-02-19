package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.admin.docs.LockerSystemConfigAdminControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.SystemConfigUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.SystemConfigService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/locker/config")
@RequiredArgsConstructor
public class LockerSystemConfigAdminController implements LockerSystemConfigAdminControllerDocs {

    private final SystemConfigService systemConfigService;

    @Override
    @GetMapping
    public ResponseEntity<SystemConfigResponse> getCurrentConfig() {
        return ResponseEntity.ok(systemConfigService.getCurrentConfig());
    }

    @Override
    @PostMapping
    public ResponseEntity<SystemConfigResponse> createConfig(
            @Valid @RequestBody SystemConfigUpdateRequest request
    ) {
        SystemConfigResponse response = systemConfigService.createConfig(request);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    @PostMapping("/application/open")
    public ResponseEntity<SystemConfigResponse> openApplication(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        SystemConfigResponse response = systemConfigService.openApplication(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/application/close")
    public ResponseEntity<SystemConfigResponse> closeApplication() {
        SystemConfigResponse response = systemConfigService.closeApplication();
        return ResponseEntity.ok(response);
    }
}
