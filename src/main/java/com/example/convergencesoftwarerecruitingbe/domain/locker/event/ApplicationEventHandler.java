package com.example.convergencesoftwarerecruitingbe.domain.locker.event;

import com.example.convergencesoftwarerecruitingbe.domain.locker.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationEventHandler {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicationCreated(ApplicationCreatedEvent event) {
        try {
            emailService.sendApplicationConfirmation(event.getApplicationId(), event.getApplicantEmail());
            emailService.sendAdminNotification(event.getApplicationId());
        } catch (Exception e) {
            log.error("신청 접수 이벤트 처리 실패. applicationId={}", event.getApplicationId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicationApproved(ApplicationApprovedEvent event) {
        try {
            emailService.sendApprovalNotification(event.getApplicationId(), event.getApplicantEmail());
        } catch (Exception e) {
            log.error("신청 승인 이벤트 처리 실패. applicationId={}", event.getApplicationId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleApplicationRejected(ApplicationRejectedEvent event) {
        try {
            emailService.sendRejectionNotification(event.getApplicationId(), event.getApplicantEmail());
        } catch (Exception e) {
            log.error("신청 거절 이벤트 처리 실패. applicationId={}", event.getApplicationId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSemesterClosed(SemesterClosedEvent event) {
        try {
            log.info("학기 종료 처리 완료: {} 건 반납", event.getReturnedRentalCount());
        } catch (Exception e) {
            log.error("학기 종료 이벤트 처리 실패", e);
        }
    }
}
