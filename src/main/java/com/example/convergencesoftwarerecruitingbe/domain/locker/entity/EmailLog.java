package com.example.convergencesoftwarerecruitingbe.domain.locker.entity;

import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailStatus;
import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.EmailType;
import com.example.convergencesoftwarerecruitingbe.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "locker_email_logs")
public class EmailLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long applicationId;

    @Column(length = 100, nullable = false)
    private String toEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailType emailType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailStatus status;

    @Column(length = 255)
    private String providerMessageId;

    @Lob
    private String failReason;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    protected EmailLog(Long applicationId, String toEmail, EmailType emailType, EmailStatus status, LocalDateTime sentAt) {
        this.applicationId = applicationId;
        this.toEmail = toEmail;
        this.emailType = emailType;
        this.status = status;
        this.sentAt = sentAt;
    }

    public static EmailLog create(Long applicationId, String toEmail, EmailType emailType) {
        return new EmailLog(applicationId, toEmail, emailType, EmailStatus.SENT, LocalDateTime.now());
    }

    public void markSent(String messageId) {
        this.status = EmailStatus.SENT;
        this.providerMessageId = messageId;
    }

    public void markFailed(String reason) {
        this.status = EmailStatus.FAILED;
        this.failReason = reason;
    }
}
