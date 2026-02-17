package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.RentalHistoryResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.RentalStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.SystemConfigRepository;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class RentalHistoryServiceTest {

    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private SystemConfigRepository systemConfigRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalService = new RentalService(
                rentalRepository, applicationRepository,
                systemConfigRepository, applicationEventPublisher
        );
    }

    private Rental createRental(Locker locker, Application application, LocalDate start, LocalDate end, boolean returned) {
        Rental rental = Rental.fromApplication(application, end);
        // rentalStartDate는 fromApplication 내부에서 LocalDate.now()로 설정되므로
        // 테스트에서는 반환된 Rental 그대로 사용
        if (returned) {
            rental.returnLocker(ReturnReason.ADMIN_FORCE);
        }
        return rental;
    }

    private Locker createLocker(int number) {
        return Locker.create(number, 1, number);
    }

    private Application createApplication(Locker locker, String studentId) {
        return Application.create(
                locker, "테스트", studentId,
                "010-0000-0000", studentId + "@test.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE
        );
    }

    @Test
    @DisplayName("기간 겹침 필터: from/to 범위와 겹치는 rental만 조회됨")
    void findRentalHistory_dateOverlapFilter() {
        // given
        Locker locker = createLocker(10);
        Application app = createApplication(locker, "20240001");
        Rental rental = Rental.fromApplication(app, LocalDate.of(2025, 6, 30));

        Pageable pageable = PageRequest.of(0, 20);
        Page<Rental> page = new PageImpl<>(List.of(rental), pageable, 1);
        given(rentalRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);

        // when
        LocalDate from = LocalDate.of(2025, 3, 1);
        LocalDate to = LocalDate.of(2025, 6, 30);
        Page<RentalHistoryResponse> result = rentalService.findRentalHistory(from, to, null, null, null, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLockerNumber()).isEqualTo(10);
        assertThat(result.getContent().get(0).getStudentId()).isEqualTo("20240001");
    }

    @Test
    @DisplayName("status=ACTIVE면 returnedAt이 null인 대여만 조회")
    void findRentalHistory_activeStatusFilter() {
        // given
        Locker locker = createLocker(5);
        Application app = createApplication(locker, "20240002");
        Rental activeRental = Rental.fromApplication(app, LocalDate.of(2025, 6, 30));
        // activeRental의 returnedAt은 null

        Pageable pageable = PageRequest.of(0, 20);
        Page<Rental> page = new PageImpl<>(List.of(activeRental), pageable, 1);
        given(rentalRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);

        // when
        Page<RentalHistoryResponse> result = rentalService.findRentalHistory(null, null, null, null, RentalStatus.ACTIVE, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getReturnedAt()).isNull();
    }

    @Test
    @DisplayName("lockerId 필터 적용 시 해당 사물함 대여만 조회")
    void findRentalHistory_lockerIdFilter() {
        // given
        Locker locker = createLocker(15);
        Application app = createApplication(locker, "20240003");
        Rental rental = Rental.fromApplication(app, LocalDate.of(2025, 6, 30));

        Pageable pageable = PageRequest.of(0, 20);
        Page<Rental> page = new PageImpl<>(List.of(rental), pageable, 1);
        given(rentalRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);

        // when
        Page<RentalHistoryResponse> result = rentalService.findRentalHistory(null, null, locker.getId(), null, null, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLockerNumber()).isEqualTo(15);
    }
}
