package com.example.convergencesoftwarerecruitingbe.domain.locker.entity;

import com.example.convergencesoftwarerecruitingbe.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "locker_system_configs")
public class SystemConfig extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean applicationOpen;

    private LocalDate applicationStartDate;

    private LocalDate applicationEndDate;

    private LocalDate semesterStartDate;

    @Column(nullable = false)
    private LocalDate semesterEndDate;

    @Column(nullable = false)
    private LocalDate rentalEndDate;

    @Column(length = 50)
    private String depositAccount;

    private Integer depositAmount;

    @Column(nullable = false)
    @ColumnDefault("3")
    private Integer depositDueDays;

    protected SystemConfig(
            Boolean applicationOpen,
            LocalDate semesterStartDate,
            LocalDate semesterEndDate,
            LocalDate rentalEndDate,
            String depositAccount,
            Integer depositAmount,
            Integer depositDueDays
    ) {
        this.applicationOpen = applicationOpen;
        this.semesterStartDate = semesterStartDate;
        this.semesterEndDate = semesterEndDate;
        this.rentalEndDate = rentalEndDate;
        this.depositAccount = depositAccount;
        this.depositAmount = depositAmount;
        this.depositDueDays = depositDueDays;
    }

    public static SystemConfig create(
            LocalDate semesterStartDate,
            LocalDate semesterEndDate,
            LocalDate rentalEndDate,
            String depositAccount,
            Integer depositAmount
    ) {
        return new SystemConfig(false, semesterStartDate, semesterEndDate, rentalEndDate, depositAccount, depositAmount, 3);
    }

    public static SystemConfig create(
            LocalDate semesterEndDate,
            LocalDate rentalEndDate,
            String depositAccount,
            Integer depositAmount
    ) {
        return create(null, semesterEndDate, rentalEndDate, depositAccount, depositAmount);
    }

    public void openApplication(LocalDate startDate, LocalDate endDate) {
        this.applicationOpen = true;
        this.applicationStartDate = startDate;
        this.applicationEndDate = endDate;
    }

    public void closeApplication() {
        this.applicationOpen = false;
    }

    public boolean isWithinApplicationWindow(LocalDate today) {
        if (!Boolean.TRUE.equals(this.applicationOpen)) {
            return false;
        }
        if (this.applicationStartDate == null || this.applicationEndDate == null) {
            return false;
        }
        return !today.isBefore(this.applicationStartDate) && !today.isAfter(this.applicationEndDate);
    }
}
