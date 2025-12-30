package com.example.convergencesoftwarerecruitingbe.domain.intro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "intro_contents")
@EntityListeners(AuditingEntityListener.class)
public class IntroContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "about_title", nullable = false)
    private String aboutTitle = "";

    @Builder.Default
    @Column(name = "about_content", columnDefinition = "TEXT", nullable = false)
    private String aboutContent = "";

    @Builder.Default
    @Column(name = "works_title", nullable = false)
    private String worksTitle = "";

    @Builder.Default
    @Column(name = "works_content", columnDefinition = "TEXT", nullable = false)
    private String worksContent = "";

    @Builder.Default
    @Column(name = "benefits_title", nullable = false)
    private String benefitsTitle = "";

    @Builder.Default
    @Column(name = "benefits_content", columnDefinition = "TEXT", nullable = false)
    private String benefitsContent = "";

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static IntroContent create(String aboutTitle, String aboutContent,
                                      String worksTitle, String worksContent,
                                      String benefitsTitle, String benefitsContent) {
        return IntroContent.builder()
                .aboutTitle(aboutTitle)
                .aboutContent(aboutContent)
                .worksTitle(worksTitle)
                .worksContent(worksContent)
                .benefitsTitle(benefitsTitle)
                .benefitsContent(benefitsContent)
                .build();
    }

    public void update(String aboutTitle, String aboutContent,
                       String worksTitle, String worksContent,
                       String benefitsTitle, String benefitsContent) {
        this.aboutTitle = aboutTitle;
        this.aboutContent = aboutContent;
        this.worksTitle = worksTitle;
        this.worksContent = worksContent;
        this.benefitsTitle = benefitsTitle;
        this.benefitsContent = benefitsContent;
    }
}
