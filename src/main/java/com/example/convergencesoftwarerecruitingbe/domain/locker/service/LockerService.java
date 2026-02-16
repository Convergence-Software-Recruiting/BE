package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.LockerResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
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

    @Transactional(readOnly = true)
    public List<LockerResponse> getAllLockers() {
        return lockerRepository.findAllByOrderByRowNoAscColNoAsc()
                .stream()
                .map(LockerResponse::from)
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

        switch (newStatus) {
            case EMPTY -> locker.makeEmpty();
            case CLOSED -> locker.close();
            case BROKEN -> locker.markBroken(adminNote);
            default -> throw new IllegalArgumentException("지원하지 않는 상태 변경입니다: " + newStatus);
        }

        return LockerResponse.from(locker);
    }

    @Transactional
    public void closeAllLockers() {
        lockerRepository.updateAllStatus(LockerStatus.CLOSED);
    }
}
