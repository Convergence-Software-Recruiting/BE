package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.SystemConfigRepository;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ApplicationWindowServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private LockerRepository lockerRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private SystemConfigRepository systemConfigRepository;
    @Mock
    private RentalRepository rentalRepository;

    private SystemConfigService systemConfigService;
    private ApplicationService applicationService;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        systemConfigService = new SystemConfigService(systemConfigRepository);
        applicationService = new ApplicationService(
                applicationRepository, lockerRepository, rentalRepository,
                applicationEventPublisher, systemConfigService
        );
        rentalService = new RentalService(
                rentalRepository, applicationRepository,
                systemConfigRepository, applicationEventPublisher
        );
    }

    private ApplicationCreateRequest createRequest(Long lockerId) {
        return new ApplicationCreateRequest(
                lockerId, "홍길동", "20240001",
                "010-1234-5678", "test@example.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE
        );
    }

    private SystemConfig createOpenConfig(LocalDate start, LocalDate end) {
        SystemConfig config = SystemConfig.create(
                LocalDate.of(2025, 6, 30),
                LocalDate.of(2025, 6, 30),
                "계좌", 5000
        );
        config.openApplication(start, end);
        return config;
    }

    private void stubWindowOpen() {
        LocalDate today = LocalDate.now();
        SystemConfig config = createOpenConfig(today.minusDays(10), today.plusDays(10));
        given(systemConfigRepository.findFirstByOrderByCreatedAtDesc())
                .willReturn(Optional.of(config));
    }

    private void stubNoDuplicates() {
        given(applicationRepository.existsByStudentIdHashAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByPhoneAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByEmailAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(rentalRepository.existsActiveRentalByStudentIdHash(anyString())).willReturn(false);
        given(rentalRepository.existsByRenterPhoneAndReturnedAtIsNull(anyString())).willReturn(false);
        given(rentalRepository.existsByRenterEmailAndReturnedAtIsNull(anyString())).willReturn(false);
    }

    // ── 신청기간 검증 ──

    @Test
    @DisplayName("신청기간 열림 + EMPTY 사물함 → 신청 성공(RESERVED)")
    void createApplication_withinWindow_success() {
        stubWindowOpen();
        stubNoDuplicates();

        Locker locker = Locker.create(2, 1, 2);
        given(lockerRepository.findById(any())).willReturn(Optional.of(locker));

        Application application = Application.create(
                locker, "홍길동", "20240001",
                "010-1234-5678", "test@example.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE
        );
        given(applicationRepository.save(any(Application.class))).willReturn(application);

        ApplicationResponse response = applicationService.createApplication(createRequest(1L));

        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.RESERVED);
    }

    @Test
    @DisplayName("신청기간 닫힘 + EMPTY 사물함 → 신청 실패(IllegalStateException)")
    void createApplication_windowClosed_throwsException() {
        SystemConfig config = SystemConfig.create(
                LocalDate.of(2025, 6, 30),
                LocalDate.of(2025, 6, 30),
                "계좌", 5000
        );
        given(systemConfigRepository.findFirstByOrderByCreatedAtDesc())
                .willReturn(Optional.of(config));

        assertThatThrownBy(() -> applicationService.createApplication(createRequest(1L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("신청 기간이 아닙니다");
    }

    @Test
    @DisplayName("신청기간 닫힘 + RESERVED 신청 → 관리자 approve 성공(IN_USE + Rental 생성)")
    void approveApplication_windowClosed_success() {
        SystemConfig config = SystemConfig.create(
                LocalDate.of(2025, 6, 30),
                LocalDate.of(2025, 6, 30),
                "계좌", 5000
        );
        given(systemConfigRepository.findFirstByOrderByCreatedAtDesc())
                .willReturn(Optional.of(config));

        Locker locker = Locker.create(2, 1, 2);
        locker.reserve();

        Application application = Application.create(
                locker, "홍길동", "20240001",
                "010-1234-5678", "test@example.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE
        );

        given(applicationRepository.findById(1L)).willReturn(Optional.of(application));

        Rental rental = Rental.fromApplication(application, config.getRentalEndDate());
        given(rentalRepository.save(any(Rental.class))).willReturn(rental);

        RentalResponse response = rentalService.approveApplication(1L, "admin");

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.APPROVED);
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.IN_USE);
        verify(rentalRepository).save(any(Rental.class));
    }

    // ── 중복 검증: 현재 활성 상태만 ──

    @Test
    @DisplayName("과거 APPROVED + rental returnedAt != null → 새 신청 가능")
    void createApplication_pastApprovedAndReturned_success() {
        stubWindowOpen();

        // SUBMITTED 없음
        given(applicationRepository.existsByStudentIdHashAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByPhoneAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByEmailAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);

        // 활성 대여 없음 (과거 대여는 returnedAt != null)
        given(rentalRepository.existsActiveRentalByStudentIdHash(anyString())).willReturn(false);
        given(rentalRepository.existsByRenterPhoneAndReturnedAtIsNull(anyString())).willReturn(false);
        given(rentalRepository.existsByRenterEmailAndReturnedAtIsNull(anyString())).willReturn(false);

        Locker locker = Locker.create(3, 2, 1);
        given(lockerRepository.findById(any())).willReturn(Optional.of(locker));

        Application application = Application.create(
                locker, "홍길동", "20240001",
                "010-1234-5678", "test@example.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE
        );
        given(applicationRepository.save(any(Application.class))).willReturn(application);

        ApplicationResponse response = applicationService.createApplication(createRequest(1L));

        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
    }

    @Test
    @DisplayName("SUBMITTED 신청 존재 → 신청 불가")
    void createApplication_pendingSubmitted_throwsException() {
        stubWindowOpen();

        given(applicationRepository.existsByStudentIdHashAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(true);

        assertThatThrownBy(() -> applicationService.createApplication(createRequest(1L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 신청 내역이 있습니다");
    }

    @Test
    @DisplayName("활성 대여(returnedAt == null) 존재 → 신청 불가")
    void createApplication_activeRental_throwsException() {
        stubWindowOpen();

        // SUBMITTED 없음
        given(applicationRepository.existsByStudentIdHashAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByPhoneAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);
        given(applicationRepository.existsByEmailAndStatus(anyString(), eq(ApplicationStatus.SUBMITTED)))
                .willReturn(false);

        // 활성 대여 있음
        given(rentalRepository.existsActiveRentalByStudentIdHash(anyString())).willReturn(true);

        assertThatThrownBy(() -> applicationService.createApplication(createRequest(1L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 신청 내역이 있습니다");
    }
}
