package com.example.convergencesoftwarerecruitingbe.domain.application.entity;

import com.example.convergencesoftwarerecruitingbe.domain.form.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_answers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"application_id", "question_id"})
})
public class ApplicationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    @Column(nullable = false)
    private String value;
}
