package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.SystemConfigUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Transactional(readOnly = true)
    public SystemConfigResponse getCurrentConfig() {
        SystemConfig config = systemConfigRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("시스템 설정이 없습니다"));

        return SystemConfigResponse.from(config);
    }

    @Transactional
    public SystemConfigResponse createConfig(SystemConfigUpdateRequest request) {
        SystemConfig config = SystemConfig.create(
                request.getSemesterStartDate(),
                request.getSemesterEndDate(),
                request.getRentalEndDate(),
                request.getDepositAccount(),
                request.getDepositAmount()
        );

        SystemConfig saved = systemConfigRepository.save(config);
        return SystemConfigResponse.from(saved);
    }

    @Transactional
    public SystemConfigResponse openApplication(LocalDate startDate, LocalDate endDate) {
        SystemConfig config = systemConfigRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("시스템 설정이 없습니다"));

        config.openApplication(startDate, endDate);
        return SystemConfigResponse.from(config);
    }

    @Transactional
    public SystemConfigResponse closeApplication() {
        SystemConfig config = systemConfigRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new IllegalStateException("시스템 설정이 없습니다"));

        config.closeApplication();
        return SystemConfigResponse.from(config);
    }

    @Transactional(readOnly = true)
    public boolean isApplicationOpen() {
        return systemConfigRepository.isApplicationOpen()
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean isApplicationWindowOpen() {
        return systemConfigRepository.findFirstByOrderByCreatedAtDesc()
                .map(config -> config.isWithinApplicationWindow(LocalDate.now()))
                .orElse(false);
    }
}
