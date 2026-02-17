package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.ActiveRentalSummaryResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockerService {

    private final LockerRepository lockerRepository;
    private final RentalRepository rentalRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public List<LockerResponse> getAllLockers() {
        return lockerRepository.findAllByOrderByRowNoAscColNoAsc()
                .stream()
                .map(LockerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LockerResponse> getAllLockersWithSummary() {
        List<Locker> lockers = lockerRepository.findAllByOrderByRowNoAscColNoAsc();

        Map<Long, Rental> activeRentalMap = rentalRepository.findAllActiveWithLocker()
                .stream()
                .collect(Collectors.toMap(Rental::getLockerId, r -> r));

        return lockers.stream()
                .map(locker -> {
                    if (locker.getStatus() == LockerStatus.IN_USE) {
                        Rental rental = activeRentalMap.get(locker.getId());
                        if (rental != null) {
                            return LockerResponse.withSummary(locker, ActiveRentalSummaryResponse.from(rental));
                        }
                    }
                    return LockerResponse.from(locker);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LockerResponse> getAvailableLockers() {
        return lockerRepository.findAllByStatusAndIsFixedFalse(LockerStatus.EMPTY)
                .stream()
                .map(LockerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<LockerStatus, Long> getLockerStatusStatistics() {
        return lockerRepository.countByStatus()
                .stream()
                .collect(Collectors.toMap(LockerRepository.LockerStatusCount::getStatus, LockerRepository.LockerStatusCount::getCount));
    }

    @Transactional
    public LockerResponse updateLockerStatus(Long lockerId, LockerStatus newStatus, String adminNote) {
        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new EntityNotFoundException("Locker not found: " + lockerId));

        // 연관 데이터 정리: EMPTY, CLOSED, BROKEN으로 변경 시 active rental/pending application 처리
        cleanupRelatedData(lockerId, newStatus);

        switch (newStatus) {
            case EMPTY -> locker.makeEmpty();
            case CLOSED -> locker.close();
            case BROKEN -> locker.markBroken(adminNote);
            default -> throw new IllegalArgumentException("지원하지 않는 상태 변경입니다: " + newStatus);
        }

        return LockerResponse.from(locker);
    }

    private void cleanupRelatedData(Long lockerId, LockerStatus newStatus) {
        // active rental 강제 반납
        rentalRepository.findByLockerIdAndReturnedAtIsNull(lockerId).ifPresent(rental -> {
            ReturnReason reason = (newStatus == LockerStatus.BROKEN)
                    ? ReturnReason.BROKEN_FORCE
                    : ReturnReason.ADMIN_FORCE;
            rental.returnLocker(reason);
            log.info("사물함 상태 변경({})으로 인한 강제 반납 처리: lockerId={}, rentalId={}, reason={}",
                    newStatus, lockerId, rental.getId(), reason);
        });

        // pending application 자동 거절
        applicationRepository.findByLockerIdAndStatus(lockerId, ApplicationStatus.SUBMITTED)
                .forEach(application -> {
                    application.reject("사물함 상태 변경(" + newStatus + ")으로 인한 자동 거절", "SYSTEM");
                    log.info("사물함 상태 변경({})으로 인한 신청 자동 거절: lockerId={}, applicationId={}",
                            newStatus, lockerId, application.getId());
                });
    }

    @Transactional
    public void closeAllLockers() {
        lockerRepository.updateAllStatus(LockerStatus.CLOSED);
    }

    @Transactional
    public int openAllLockers() {
        int count = lockerRepository.bulkOpenAllLockers();
        log.info("사물함 일괄 오픈 처리: {} 건", count);
        return count;
    }
}
