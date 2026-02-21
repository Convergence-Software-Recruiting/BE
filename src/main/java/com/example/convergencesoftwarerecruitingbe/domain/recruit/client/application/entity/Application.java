package com.example.convergencesoftwarerecruitingbe.domain.recruit.client.application.entity;

import com.example.convergencesoftwarerecruitingbe.domain.recruit.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.global.common.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"form_id", "student_no"}),
                @UniqueConstraint(columnNames = {"form_id", "result_code"})
        }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(nullable = false)
    private String name;

    @Column(name = "student_no", nullable = false)
    private String studentNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING) // Enum 적용
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department firstChoice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department secondChoice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department thirdChoice;

    @Column(name = "result_code", nullable = false, length = 4, updatable = false)
    private String resultCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.RECEIVED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Lob
    private String adminMemo;

    @PrePersist
    protected void onSubmit() {
        this.submittedAt = LocalDateTime.now();
    }

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void updateAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo;
    }
}
