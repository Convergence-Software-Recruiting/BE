package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LockerServiceOpenAllTest {

    @Mock
    LockerRepository lockerRepository;

    @Mock
    RentalRepository rentalRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    LockerService lockerService;

    @Test
    @DisplayName("CLOSED + isFixed=false 사물함만 EMPTY로 변경 — bulkOpenAllLockers 호출 검증")
    void openAllLockers_callsBulkAndReturnsCount() {
        // given
        given(lockerRepository.bulkOpenAllLockers()).willReturn(32);

        // when
        int count = lockerService.openAllLockers();

        // then
        assertThat(count).isEqualTo(32);
        verify(lockerRepository).bulkOpenAllLockers();
    }

    @Test
    @DisplayName("CLOSED 사물함이 없으면 0건 반환")
    void openAllLockers_noClosedLockers_returnsZero() {
        // given
        given(lockerRepository.bulkOpenAllLockers()).willReturn(0);

        // when
        int count = lockerService.openAllLockers();

        // then
        assertThat(count).isEqualTo(0);
    }
}
