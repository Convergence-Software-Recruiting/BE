package com.example.convergencesoftwarerecruitingbe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request.ApplicationCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ApplicationResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.SystemConfig;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.SystemConfigRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.service.ApplicationService;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ApplicationServiceTest {

    @Autowired
    ApplicationService applicationService;
    @Autowired
    LockerRepository lockerRepository;
    @Autowired
    SystemConfigRepository systemConfigRepository;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    RentalRepository rentalRepository;

    private String uniqueStudentId;
    private String uniquePhone;
    private String uniqueEmail;

    @BeforeEach
    void setUp() {
        // 신청 기간이 열린 SystemConfig 생성
        SystemConfig config = SystemConfig.create(
                LocalDate.now().plusMonths(3),
                LocalDate.now().plusMonths(3),
                "테스트계좌", 5000
        );
        config.openApplication(LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        systemConfigRepository.save(config);

        // 테스트마다 고유한 값 사용
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        uniqueStudentId = "T" + suffix;
        uniquePhone = "010-" + suffix;
        uniqueEmail = suffix + "@test.com";
    }

    @Test
    @DisplayName("신청 생성 - 정상")
    void createApplication_success() {
        // given
        Locker locker = Locker.create(99, 10, 10);
        lockerRepository.save(locker);

        ApplicationCreateRequest request = new ApplicationCreateRequest(
                locker.getId(), "홍길동", uniqueStudentId,
                uniquePhone, uniqueEmail,
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE, "000"
        );

        // when
        ApplicationResponse response = applicationService.createApplication(request);

        // then
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
        assertThat(response.getApplicantName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("중복 신청 - 예외 발생")
    void createApplication_duplicate_throwsException() {
        // given
        Locker locker = Locker.create(99, 10, 10);
        lockerRepository.save(locker);

        ApplicationCreateRequest request = new ApplicationCreateRequest(
                locker.getId(), "홍길동", uniqueStudentId,
                uniquePhone, uniqueEmail,
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE, "000"
        );

        // 첫 번째 신청 (성공)
        applicationService.createApplication(request);

        // when & then - 같은 학번으로 재신청
        Locker locker2 = Locker.create(98, 10, 11);
        lockerRepository.save(locker2);

        ApplicationCreateRequest duplicateRequest = new ApplicationCreateRequest(
                locker2.getId(), "홍길동2", uniqueStudentId,
                "010-9999-0000", "other@test.com",
                Grade.GRADE_3, Major.CONVERGENCE_SOFTWARE, "000"
        );

        assertThatThrownBy(() -> applicationService.createApplication(duplicateRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 신청 내역이 있습니다");
    }
}
