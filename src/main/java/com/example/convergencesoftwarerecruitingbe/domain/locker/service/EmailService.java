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
import org.springframework.beans.factory.annotation.Value;
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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DEFAULT_LOCKER_LOCATION = "종합관 3층 인공지능소프트웨어융합대학교학팀 앞";

    private final AdminMailProperties adminMailProperties;
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
        SystemConfigResponse config = systemConfigService.getCurrentConfig();

        EmailLog emailLog = EmailLog.create(applicationId, toEmail, EmailType.APPLIED_USER);

        try {
            Context context = new Context();
            context.setVariable("applicantName", app.getApplicantName());
            context.setVariable("lockerNumber", app.getLocker().getNumber());
            context.setVariable("appliedAt", app.getCreatedAt().format(DATE_TIME_FORMATTER));
            context.setVariable("grade", formatGrade(app.getGrade().name()));
            context.setVariable("major", formatMajor(app.getMajor().name()));
            context.setVariable("phone", app.getPhone());
            context.setVariable("depositAccount", defaultValue(config.getDepositAccount(), "미정"));
            context.setVariable("depositAmount", formatAmount(config.getDepositAmount()));
            context.setVariable("depositDueDays", config.getDepositDueDays() == null ? 3 : config.getDepositDueDays());

            String htmlContent = templateEngine.process("email/application-confirmation", context);
            String plainText = buildApplicationConfirmationPlainText(
                    app.getApplicantName(),
                    app.getLocker().getNumber(),
                    app.getCreatedAt().format(DATE_TIME_FORMATTER),
                    formatGrade(app.getGrade().name()),
                    formatMajor(app.getMajor().name()),
                    app.getPhone()
            );
            String messageId = sendEmail(toEmail, "[사물함] 신청이 접수되었습니다", plainText, htmlContent);
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

        EmailLog emailLog = EmailLog.create(applicationId, adminMailProperties.getEmail(), EmailType.APPLIED_ADMIN);

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
            context.setVariable("lockerPassword", app.getLockerPassword());

            String htmlContent = templateEngine.process("email/admin-notification", context);
            String plainText = buildAdminNotificationPlainText(
                    app.getApplicantName(),
                    app.getStudentId(),
                    formatGrade(app.getGrade().name()),
                    formatMajor(app.getMajor().name()),
                    app.getPhone(),
                    app.getEmail(),
                    app.getLocker().getNumber(),
                    app.getCreatedAt().format(DATE_TIME_FORMATTER),
                    app.getLockerPassword()
            );
            String messageId = sendEmail(adminMailProperties.getEmail(), "[관리자] 신규 사물함 신청", plainText, htmlContent);
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
            context.setVariable("lockerLocation", DEFAULT_LOCKER_LOCATION);
            context.setVariable("lockerPassword", app.getLockerPassword());

            String htmlContent = templateEngine.process("email/application-approved", context);
            String plainText = buildApprovalNotificationPlainText(
                    app.getApplicantName(),
                    app.getLocker().getNumber(),
                    rental.getRentalStartDate().format(DATE_FORMATTER),
                    rental.getRentalEndDate().format(DATE_FORMATTER),
                    rental.getApprovedAt().format(DATE_TIME_FORMATTER),
                    defaultValue(config.getDepositAccount(), "미정"),
                    formatAmount(config.getDepositAmount()),
                    config.getDepositDueDays() == null ? 3 : config.getDepositDueDays(),
                    app.getLockerPassword()
            );
            String messageId = sendEmail(toEmail, "[사물함] 신청이 승인되었습니다", plainText, htmlContent);
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
            String plainText = buildRejectionNotificationPlainText(
                    app.getApplicantName(),
                    app.getLocker().getNumber(),
                    app.getCreatedAt().format(DATE_TIME_FORMATTER),
                    app.getDecidedAt() == null ? "-" : app.getDecidedAt().format(DATE_TIME_FORMATTER),
                    defaultValue(app.getDecisionReason(), "사유 미기재")
            );
            String messageId = sendEmail(toEmail, "[사물함] 신청 결과 안내", plainText, htmlContent);
            emailLog.markSent(messageId);
        } catch (Exception e) {
            log.error("거절 메일 발송 실패: {}", e.getMessage(), e);
            emailLog.markFailed(e.getMessage());
        } finally {
            emailLogRepository.save(emailLog);
        }
    }

    private String sendEmail(String to, String subject, String plainText, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(plainText, htmlContent);
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

    private String buildApplicationConfirmationPlainText(
            String applicantName,
            Integer lockerNumber,
            String appliedAt,
            String grade,
            String major,
            String phone
    ) {
        return "사물함 신청 접수 완료\n"
                + "안녕하세요, " + applicantName + "님.\n"
                + "사물함 신청이 정상적으로 접수되었습니다.\n\n"
                + "[신청 정보]\n"
                + "- 사물함 번호: " + lockerNumber + "번\n"
                + "- 신청 일시: " + appliedAt + "\n"
                + "- 학년: " + grade + "\n"
                + "- 전공: " + major + "\n"
                + "- 연락처: " + phone + "\n\n"
                + "사물함 관리 시스템\n"
                + "자동 발송 메일입니다.";
    }

    private String buildAdminNotificationPlainText(
            String applicantName,
            String studentId,
            String grade,
            String major,
            String phone,
            String email,
            Integer lockerNumber,
            String appliedAt,
            String lockerPassword
    ) {
        return "신규 사물함 신청\n"
                + "새로운 신청이 접수되었습니다.\n\n"
                + "[신청자 정보]\n"
                + "- 이름: " + applicantName + "\n"
                + "- 학번: " + studentId + "\n"
                + "- 학년: " + grade + "\n"
                + "- 전공: " + major + "\n"
                + "- 연락처: " + phone + "\n"
                + "- 이메일: " + email + "\n\n"
                + "[사물함 정보]\n"
                + "- 사물함 번호: " + lockerNumber + "번\n"
                + "- 신청 일시: " + appliedAt + "\n"
                + "- 신청 비밀번호: " + lockerPassword + "\n\n"
                + "사물함 관리 시스템\n"
                + "자동 발송 메일입니다.";
    }

    private String buildApprovalNotificationPlainText(
            String applicantName,
            Integer lockerNumber,
            String rentalStartDate,
            String rentalEndDate,
            String approvedAt,
            String depositAccount,
            String depositAmount,
            Integer depositDueDays,
            String lockerPassword
    ) {
        return "사물함 신청 승인\n"
                + "안녕하세요, " + applicantName + "님.\n"
                + "신청하신 사물함이 승인되었습니다.\n\n"
                + "[대여 정보]\n"
                + "- 사물함 번호: " + lockerNumber + "번\n"
                + "- 비밀번호: " + lockerPassword + "\n"
                + "- 대여 시작일: " + rentalStartDate + "\n"
                + "- 반납 기한: " + rentalEndDate + "\n"
                + "- 승인 일시: " + approvedAt + "\n\n"
                + "[보증금 안내]\n"
                + "- 입금 계좌: " + depositAccount + "\n"
                + "- 입금 금액: " + depositAmount + "원\n"
                + "- 입금 기한: 승인일로부터 " + depositDueDays + "일 이내\n\n"
                + "사물함 관리 시스템\n"
                + "자동 발송 메일입니다.";
    }

    private String buildRejectionNotificationPlainText(
            String applicantName,
            Integer lockerNumber,
            String appliedAt,
            String rejectedAt,
            String decisionReason
    ) {
        return "사물함 신청 결과 안내\n"
                + "안녕하세요, " + applicantName + "님.\n"
                + "사물함 신청이 거절되었습니다.\n\n"
                + "[거절 사유]\n"
                + decisionReason + "\n\n"
                + "[신청 정보]\n"
                + "- 사물함 번호: " + lockerNumber + "번\n"
                + "- 신청 일시: " + appliedAt + "\n"
                + "- 거절 일시: " + rejectedAt + "\n\n"
                + "사물함 관리 시스템\n"
                + "자동 발송 메일입니다.";
    }
}
