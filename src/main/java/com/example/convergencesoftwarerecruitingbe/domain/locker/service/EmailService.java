package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.EmailLog;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailType;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.EmailLogRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private static final String ADMIN_EMAIL = "admin@example.com";

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final ApplicationRepository applicationRepository;
    private final RentalRepository rentalRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendApplicationConfirmation(Long applicationId, String toEmail) {
        Application application = getApplication(applicationId);
        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.APPLIED_USER);

        String subject = "[사물함] 신청이 접수되었습니다";
        String content = "신청자: " + application.getApplicantName() + "<br/>"
                + "사물함 번호: " + application.getLocker().getNumber() + "번<br/>"
                + "신청 일시: " + application.getCreatedAt();

        sendWithLog(emailLog, toEmail, subject, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAdminNotification(Long applicationId) {
        Application application = getApplication(applicationId);
        EmailLog emailLog = EmailLog.create(applicationId, ADMIN_EMAIL, EmailType.APPLIED_ADMIN);

        String subject = "[사물함 관리] 새로운 신청이 접수되었습니다";
        String content = "신청자: " + application.getApplicantName() + "<br/>"
                + "이메일: " + application.getEmail() + "<br/>"
                + "연락처: " + application.getPhone() + "<br/>"
                + "사물함 번호: " + application.getLocker().getNumber() + "번";

        sendWithLog(emailLog, ADMIN_EMAIL, subject, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendApprovalNotification(Long applicationId, String toEmail) {
        Application application = getApplication(applicationId);
        Rental rental = rentalRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found: " + applicationId));
        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.APPROVED_USER);

        String subject = "[사물함] 신청이 승인되었습니다";
        String content = "사물함 번호: " + application.getLocker().getNumber() + "번<br/>"
                + "대여 기간: " + rental.getRentalStartDate() + " ~ " + rental.getRentalEndDate() + "<br/>"
                + "입금 정보: 학생회 안내 메시지를 확인해주세요.";

        sendWithLog(emailLog, toEmail, subject, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendRejectionNotification(Long applicationId, String toEmail) {
        Application application = getApplication(applicationId);
        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.REJECTED_USER);

        String subject = "[사물함] 신청이 거절되었습니다";
        String content = "신청자: " + application.getApplicantName() + "<br/>"
                + "거절 사유: " + application.getDecisionReason();

        sendWithLog(emailLog, toEmail, subject, content);
    }

    private void sendWithLog(EmailLog emailLog, String toEmail, String subject, String bodyContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, toEmail);
            mimeMessage.setSubject(subject);
            mimeMessage.setText(buildHtml(subject, bodyContent), "UTF-8", "html");

            mailSender.send(mimeMessage);
            emailLog.markSent(mimeMessage.getMessageID());
        } catch (Exception e) {
            log.error("메일 발송 실패. toEmail={}", toEmail, e);
            emailLog.markFailed(e.getMessage());
        }

        emailLogRepository.save(emailLog);
    }

    private Application getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application not found: " + applicationId));
    }

    private String buildHtml(String title, String content) {
        return "<html>"
                + "<body>"
                + "<h2>" + title + "</h2>"
                + "<p>" + content + "</p>"
                + "<hr>"
                + "<small>문의: 학생회 이메일</small>"
                + "</body>"
                + "</html>";
    }
}
