package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Locker;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockerSeeder {

    private final LockerRepository lockerRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedLockers() {

        long existingCount = lockerRepository.count();
        if (existingCount > 0) {
            log.info("사물함이 이미 {}개 존재합니다. Seeding을 건너뜁니다.", existingCount);
            return;
        }

        log.info("사물함 초기 데이터 생성 시작...");

        int lockerNumber = 1;
        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 14; col++) {
                Locker locker = Locker.create(lockerNumber, row, col);
                lockerRepository.save(locker);

                if (lockerNumber == 1) {
                    log.info("사물함 1번 생성 완료 - 학생회 전용 (isFixed={}, status={})",
                            locker.getIsFixed(), locker.getStatus());
                } else if (lockerNumber % 10 == 0) {
                    log.debug("사물함 {}번까지 생성 완료...", lockerNumber);
                }

                lockerNumber++;
            }
        }

        log.info("✅ 사물함 56개 생성 완료!");
        log.info("   - 1번: 학생회 전용 (CLOSED)");
        log.info("   - 2~56번: 일반 사물함 (EMPTY)");
    }
}