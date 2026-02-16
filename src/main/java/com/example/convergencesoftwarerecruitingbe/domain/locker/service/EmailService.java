package com.example.convergencesoftwarerecruitingbe.domain.locker.service;

import com.example.convergencesoftwarerecruitingbe.domain.locker.dto.response.SystemConfigResponse;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Application;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.EmailLog;
import com.example.convergencesoftwarerecruitingbe.domain.locker.entity.Rental;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailType;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.ApplicationRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.EmailLogRepository;
import com.example.convergencesoftwarerecruitingbe.domain.locker.repository.RentalRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ApplicationRepository applicationRepository;
    private final RentalRepository rentalRepository;
    private final EmailLogRepository emailLogRepository;
    private final SystemConfigService systemConfigService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendApplicationConfirmation(Long applicationId, String toEmail) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.APPLIED_USER);

        try {
            Context context = new Context();
            context.setVariable("applicantName", app.getApplicantName());
            context.setVariable("lockerNumber", app.getLocker().getNumber());
            context.setVariable("appliedAt", app.getCreatedAt().format(DATE_TIME_FORMATTER));
            context.setVariable("grade", formatGrade(app.getGrade().name()));
            context.setVariable("major", formatMajor(app.getMajor().name()));
            context.setVariable("phone", app.getPhone());

            String htmlContent = templateEngine.process("email/application-confirmation", context);
            String messageId = sendEmail(toEmail, "[사물함] 신청이 접수되었습니다", htmlContent);
            emailLog.markSent(messageId);
        } catch (Exception e) {
            log.error("메일 발송 실패: {}", e.getMessage(), e);
            emailLog.markFailed(e.getMessage());
        } finally {
            emailLogRepository.save(emailLog);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAdminNotification(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        EmailLog emailLog = EmailLog.create(applicationId, ADMIN_EMAIL, EmailType.APPLIED_ADMIN);

        try {
            Context context = new Context();
            context.setVariable("applicantName", app.getApplicantName());
            context.setVariable("studentId", app.getStudentId());
            context.setVariable("grade", formatGrade(app.getGrade().name()));
            context.setVariable("major", formatMajor(app.getMajor().name()));
            context.setVariable("phone", app.getPhone());
            context.setVariable("email", app.getEmail());
            context.setVariable("lockerNumber", app.getLocker().getNumber());
            context.setVariable("appliedAt", app.getCreatedAt().format(DATE_TIME_FORMATTER));

            String htmlContent = templateEngine.process("email/admin-notification", context);
            String messageId = sendEmail(ADMIN_EMAIL, "[관리자] 신규 사물함 신청", htmlContent);
            emailLog.markSent(messageId);
        } catch (Exception e) {
            log.error("관리자 알림 메일 발송 실패: {}", e.getMessage(), e);
            emailLog.markFailed(e.getMessage());
        } finally {
            emailLogRepository.save(emailLog);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendApprovalNotification(Long applicationId, String toEmail) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        Rental rental = rentalRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("대여 정보를 찾을 수 없습니다"));

        SystemConfigResponse config = systemConfigService.getCurrentConfig();

        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.APPROVED_USER);

        try {
            Context context = new Context();
            context.setVariable("applicantName", app.getApplicantName());
            context.setVariable("lockerNumber", app.getLocker().getNumber());
            context.setVariable("rentalStartDate", rental.getRentalStartDate().format(DATE_FORMATTER));
            context.setVariable("rentalEndDate", rental.getRentalEndDate().format(DATE_FORMATTER));
            context.setVariable("approvedAt", rental.getApprovedAt().format(DATE_TIME_FORMATTER));
            context.setVariable("depositAccount", defaultValue(config.getDepositAccount(), "미정"));
            context.setVariable("depositAmount", formatAmount(config.getDepositAmount()));
            context.setVariable("depositDueDays", config.getDepositDueDays() == null ? 3 : config.getDepositDueDays());

            String htmlContent = templateEngine.process("email/application-approved", context);
            String messageId = sendEmail(toEmail, "[사물함] 신청이 승인되었습니다", htmlContent);
            emailLog.markSent(messageId);
        } catch (Exception e) {
            log.error("승인 메일 발송 실패: {}", e.getMessage(), e);
            emailLog.markFailed(e.getMessage());
        } finally {
            emailLogRepository.save(emailLog);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendRejectionNotification(Long applicationId, String toEmail) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청서를 찾을 수 없습니다"));

        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.REJECTED_USER);

        try {
            Context context = new Context();
            context.setVariable("applicantName", app.getApplicantName());
            context.setVariable("lockerNumber", app.getLocker().getNumber());
            context.setVariable("appliedAt", app.getCreatedAt().format(DATE_TIME_FORMATTER));
            context.setVariable("rejectedAt", app.getDecidedAt() == null ? "-" : app.getDecidedAt().format(DATE_TIME_FORMATTER));
            context.setVariable("decisionReason", defaultValue(app.getDecisionReason(), "사유 미기재"));

            String htmlContent = templateEngine.process("email/application-rejected", context);
            String messageId = sendEmail(toEmail, "[사물함] 신청 결과 안내", htmlContent);
            emailLog.markSent(messageId);
        } catch (Exception e) {
            log.error("거절 메일 발송 실패: {}", e.getMessage(), e);
            emailLog.markFailed(e.getMessage());
        } finally {
            emailLogRepository.save(emailLog);
        }
    }

    private String sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@example.com");

        mailSender.send(message);
        log.info("메일 발송 성공: {}", to);
        return message.getMessageID();
    }

    private String formatGrade(String grade) {
        return grade.replace("GRADE_", "") + "학년";
    }

    private String formatMajor(String major) {
        return switch (major) {
            case "CONVERGENCE_SOFTWARE" -> "융합소프트웨어";
            case "APPLIED_SOFTWARE" -> "응용소프트웨어";
            case "DATA_SCIENCE" -> "데이터사이언스";
            case "ARTIFICIAL_INTELLIGENCE" -> "인공지능";
            default -> major;
        };
    }

    private String formatAmount(Integer amount) {
        if (amount == null) {
            return "0";
        }
        return String.format("%,d", amount);
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
