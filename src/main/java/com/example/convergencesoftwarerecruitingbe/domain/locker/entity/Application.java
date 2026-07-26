package com.example.convergencesoftwarerecruitingbe.domain.locker.entity;

import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
import com.example.convergencesoftwarerecruitingbe.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Getter
@Entity(name = "LockerApplication")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "locker_applications")
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locker_id", nullable = false)
    private Long lockerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", nullable = false, insertable = false, updatable = false)
    private Locker locker;

    @Column(length = 100, nullable = false)
    private String applicantName;

    @Column(length = 20, nullable = false)
    private String studentId;

    @Column(length = 64, nullable = false)
    private String studentIdHash;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Column(length = 3, nullable = false)
    private String lockerPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private LocalDateTime decidedAt;

    @Column(length = 500)
    private String decisionReason;

    @Column(length = 100)
    private String decidedBy;

    protected Application(
            Long lockerId,
            Locker locker,
            String applicantName,
            String studentId,
            String studentIdHash,
            String phone,
            String email,
            Grade grade,
            Major major,
            String lockerPassword,
            ApplicationStatus status
    ) {
        this.lockerId = lockerId;
        this.locker = locker;
        this.applicantName = applicantName;
        this.studentId = studentId;
        this.studentIdHash = studentIdHash;
        this.phone = phone;
        this.email = email;
        this.grade = grade;
        this.major = major;
        this.lockerPassword = lockerPassword;
        this.status = status;
    }

    public static Application create(
            Locker locker,
            String name,
            String studentId,
            String phone,
            String email,
            Grade grade,
            Major major,
            String lockerPassword
    ) {
        return new Application(
                locker.getId(),
                locker,
                name,
                studentId,
                hashStudentId(studentId),
                phone,
                email,
                grade,
                major,
                lockerPassword,
                ApplicationStatus.SUBMITTED
        );
    }

    public void approve(String decidedBy) {
        if (this.status != ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("제출된 신청서만 승인할 수 있습니다");
        }
        this.status = ApplicationStatus.APPROVED;
        this.decidedAt = LocalDateTime.now();
        this.decidedBy = decidedBy;
    }

    public void reject(String reason, String decidedBy) {
        if (this.status != ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("제출된 신청서만 거절할 수 있습니다");
        }
        this.status = ApplicationStatus.REJECTED;
        this.decisionReason = reason;
        this.decidedAt = LocalDateTime.now();
        this.decidedBy = decidedBy;
    }

    private static String hashStudentId(String studentId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(studentId.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte hashByte : hashBytes) {
                sb.append(String.format("%02x", hashByte));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 해시 생성에 실패했습니다", e);
        }
    }
}
