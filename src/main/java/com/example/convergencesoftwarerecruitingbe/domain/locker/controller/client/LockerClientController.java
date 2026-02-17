package com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client;

import com.example.convergencesoftwarerecruitingbe.domain.locker.controller.client.docs.LockerClientControllerDocs;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.LockerService;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.SystemConfigService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/client/locker")
@RequiredArgsConstructor
public class LockerClientController implements LockerClientControllerDocs {

    private final LockerService lockerService;
    private final SystemConfigService systemConfigService;

    @Override
    @GetMapping
    public ResponseEntity<List<LockerResponse>> getAllLockers() {
        return ResponseEntity.ok(lockerService.getAllLockersWithSummary());
    }

    @Override
    @GetMapping("/available")
    public ResponseEntity<List<LockerResponse>> getAvailableLockers() {
        return ResponseEntity.ok(lockerService.getAvailableLockers());
    }

    @Override
    @GetMapping("/config")
    public ResponseEntity<SystemConfigResponse> getCurrentConfig() {
        return ResponseEntity.ok(systemConfigService.getCurrentConfig());
    }
}
