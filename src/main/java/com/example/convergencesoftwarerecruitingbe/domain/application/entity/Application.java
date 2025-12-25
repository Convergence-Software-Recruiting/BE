package com.example.convergencesoftwarerecruitingbe.domain.application.entity;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import com.example.convergencesoftwarerecruitingbe.global.common.ApplicationStatus;
import com.example.convergencesoftwarerecruitingbe.global.common.Grade;
import com.example.convergencesoftwarerecruitingbe.global.common.Major;
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

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"form_id", "student_no"})
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
}
