package com.example.convergencesoftwarerecruitingbe.domain.application.entity;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.FormVersion;
import jakarta.persistence.*;
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
                @UniqueConstraint(columnNames = {"form_version_id", "student_no"})
        }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "form_version_id", nullable = false)
    private FormVersion formVersion;

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
    private String email;

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

    public void changeStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void changeAdminMemo(String memo) {
        this.adminMemo = memo;
    }
}