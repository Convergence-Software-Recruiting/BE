package com.example.convergencesoftwarerecruitingbe.domain.locker.entity;

import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.ReturnReason;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "locker_rentals")
public class Rental extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locker_id", nullable = false)
    private Long lockerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", nullable = false, insertable = false, updatable = false)
    private Locker locker;

    @Column(name = "application_id", nullable = false, unique = true)
    private Long applicationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, insertable = false, updatable = false)
    private Application application;

    @Column(length = 100, nullable = false)
    private String renterName;

    @Column(length = 20, nullable = false)
    private String renterPhone;

    @Column(length = 100, nullable = false)
    private String renterEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade renterGrade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major renterMajor;

    @Column(nullable = false)
    private LocalDate rentalStartDate;

    @Column(nullable = false)
    private LocalDate rentalEndDate;

    @Column(nullable = false)
    private LocalDateTime approvedAt;

    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    private ReturnReason returnReason;

    protected Rental(
            Long lockerId,
            Locker locker,
            Long applicationId,
            Application application,
            String renterName,
            String renterPhone,
            String renterEmail,
            Grade renterGrade,
            Major renterMajor,
            LocalDate rentalStartDate,
            LocalDate rentalEndDate,
            LocalDateTime approvedAt
    ) {
        this.lockerId = lockerId;
        this.locker = locker;
        this.applicationId = applicationId;
        this.application = application;
        this.renterName = renterName;
        this.renterPhone = renterPhone;
        this.renterEmail = renterEmail;
        this.renterGrade = renterGrade;
        this.renterMajor = renterMajor;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.approvedAt = approvedAt;
    }

    public static Rental fromApplication(Application app, LocalDate rentalEndDate) {
        return new Rental(
                app.getLockerId(),
                app.getLocker(),
                app.getId(),
                app,
                app.getApplicantName(),
                app.getPhone(),
                app.getEmail(),
                app.getGrade(),
                app.getMajor(),
                LocalDate.now(),
                rentalEndDate,
                LocalDateTime.now()
        );
    }

    public void returnLocker(ReturnReason reason) {
        if (this.returnedAt != null) {
            throw new IllegalStateException("이미 반납 처리된 대여입니다");
        }
        this.returnedAt = LocalDateTime.now();
        this.returnReason = reason;
    }
}
