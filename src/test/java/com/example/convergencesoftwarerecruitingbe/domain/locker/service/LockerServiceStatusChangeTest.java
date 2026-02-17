package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LockerServiceStatusChangeTest {

    @Mock
    LockerRepository lockerRepository;

    @Mock
    RentalRepository rentalRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    LockerService lockerService;

    @Test
    @DisplayName("IN_USE + active rental → EMPTY 변경 시 rental이 ADMIN_FORCE로 강제 반납됨")
    void updateStatus_inUseToEmpty_forcesReturnRental() {
        // given
        Locker locker = createInUseLocker(1L);
        Application application = createApplication(locker);
        Rental rental = Rental.fromApplication(application, LocalDate.now().plusMonths(3));

        given(lockerRepository.findById(1L)).willReturn(Optional.of(locker));
        given(rentalRepository.findByLockerIdAndReturnedAtIsNull(1L)).willReturn(Optional.of(rental));
        given(applicationRepository.findByLockerIdAndStatus(1L, ApplicationStatus.SUBMITTED))
                .willReturn(Collections.emptyList());

        // when
        lockerService.updateLockerStatus(1L, LockerStatus.EMPTY, null);

        // then
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.EMPTY);
        assertThat(rental.getReturnedAt()).isNotNull();
        assertThat(rental.getReturnReason()).isEqualTo(ReturnReason.ADMIN_FORCE);
    }

    @Test
    @DisplayName("RESERVED + pending application → BROKEN 변경 시 application이 자동 거절됨")
    void updateStatus_reservedToBroken_rejectsApplication() {
        // given
        Locker locker = createReservedLocker(2L);
        Application application = createApplication(locker);

        given(lockerRepository.findById(2L)).willReturn(Optional.of(locker));
        given(rentalRepository.findByLockerIdAndReturnedAtIsNull(2L)).willReturn(Optional.empty());
        given(applicationRepository.findByLockerIdAndStatus(2L, ApplicationStatus.SUBMITTED))
                .willReturn(List.of(application));

        // when
        lockerService.updateLockerStatus(2L, LockerStatus.BROKEN, "고장");

        // then
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.BROKEN);
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
        assertThat(application.getDecisionReason()).contains("자동 거절");
    }

    @Test
    @DisplayName("EMPTY 상태 (연관 데이터 없음) → CLOSED 변경 시 정상 처리")
    void updateStatus_emptyToClosed_noRelatedData() {
        // given
        Locker locker = Locker.create(10, 1, 1);
        setLockerId(locker, 3L);

        given(lockerRepository.findById(3L)).willReturn(Optional.of(locker));
        given(rentalRepository.findByLockerIdAndReturnedAtIsNull(3L)).willReturn(Optional.empty());
        given(applicationRepository.findByLockerIdAndStatus(3L, ApplicationStatus.SUBMITTED))
                .willReturn(Collections.emptyList());

        // when
        lockerService.updateLockerStatus(3L, LockerStatus.CLOSED, null);

        // then
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.CLOSED);
    }

    @Test
    @DisplayName("IN_USE → EMPTY 변경 후 locker/rental 상태 종합 검증")
    void updateStatus_inUseToEmpty_verifyAllStates() {
        // given
        Locker locker = createInUseLocker(4L);
        Application application = createApplication(locker);
        Rental rental = Rental.fromApplication(application, LocalDate.now().plusMonths(3));

        given(lockerRepository.findById(4L)).willReturn(Optional.of(locker));
        given(rentalRepository.findByLockerIdAndReturnedAtIsNull(4L)).willReturn(Optional.of(rental));
        given(applicationRepository.findByLockerIdAndStatus(4L, ApplicationStatus.SUBMITTED))
                .willReturn(Collections.emptyList());

        // when
        lockerService.updateLockerStatus(4L, LockerStatus.EMPTY, null);

        // then
        assertThat(locker.getStatus()).isEqualTo(LockerStatus.EMPTY);
        assertThat(rental.getReturnedAt()).isNotNull();
        assertThat(rental.getReturnReason()).isEqualTo(ReturnReason.ADMIN_FORCE);
    }

    // --- helper methods ---

    private Locker createInUseLocker(Long id) {
        Locker locker = Locker.create(100 + id.intValue(), 1, 1);
        setLockerId(locker, id);
        locker.reserve();
        locker.markInUse();
        return locker;
    }

    private Locker createReservedLocker(Long id) {
        Locker locker = Locker.create(100 + id.intValue(), 1, 1);
        setLockerId(locker, id);
        locker.reserve();
        return locker;
    }

    private Application createApplication(Locker locker) {
        return Application.create(locker, "테스트", "20241234", "010-1234-5678",
                "test@test.com", Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE);
    }

    private void setLockerId(Locker locker, Long id) {
        try {
            java.lang.reflect.Field idField = Locker.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(locker, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
