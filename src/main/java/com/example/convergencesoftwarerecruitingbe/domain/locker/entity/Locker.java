package com.example.convergencesoftwarerecruitingbe.domain.locker.entity;

import com.example.convergencesoftwarerecruitingbe.domain.locker.enums.LockerStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "lockers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"row_no", "col_no"})
        }
)
public class Locker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(name = "row_no", nullable = false)
    private Integer rowNo;

    @Column(name = "col_no", nullable = false)
    private Integer colNo;

    @Column(name = "is_fixed", nullable = false)
    private Boolean isFixed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LockerStatus status;

    @Column(length = 500)
    private String adminNote;

    @Version
    private Long version;

    protected Locker(Integer number, Integer rowNo, Integer colNo, Boolean isFixed, LockerStatus status) {
        this.number = number;
        this.rowNo = rowNo;
        this.colNo = colNo;
        this.isFixed = isFixed;
        this.status = status;
    }

    public static Locker create(Integer number, Integer rowNo, Integer colNo) {
        if (Integer.valueOf(1).equals(number)) {
            return new Locker(number, rowNo, colNo, true, LockerStatus.CLOSED);
        }
        return new Locker(number, rowNo, colNo, false, LockerStatus.EMPTY);
    }

    public void reserve() {
        if (Boolean.TRUE.equals(this.isFixed)) {
            throw new IllegalStateException("고정 사물함은 예약할 수 없습니다");
        }
        if (this.status != LockerStatus.EMPTY) {
            throw new IllegalStateException("빈 사물함만 예약할 수 있습니다");
        }
        this.status = LockerStatus.RESERVED;
    }

    public void markInUse() {
        if (this.status != LockerStatus.RESERVED) {
            throw new IllegalStateException("예약된 사물함만 사용 상태로 변경할 수 있습니다");
        }
        this.status = LockerStatus.IN_USE;
    }

    public void makeEmpty() {
        this.status = LockerStatus.EMPTY;
    }

    public void close() {
        this.status = LockerStatus.CLOSED;
    }

    public void markBroken(String note) {
        this.status = LockerStatus.BROKEN;
        this.adminNote = note;
    }
}
