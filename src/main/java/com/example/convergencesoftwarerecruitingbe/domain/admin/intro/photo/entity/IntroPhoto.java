package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity;

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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "intro_photos")
@EntityListeners(AuditingEntityListener.class)
public class IntroPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String objectKey;

    @Column(nullable = false)
    private String publicUrl;

    @Builder.Default
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static IntroPhoto create(String objectKey, String publicUrl, Integer sortOrder) {
        return IntroPhoto.builder()
                .objectKey(objectKey)
                .publicUrl(publicUrl)
                .sortOrder(sortOrder == null ? 0 : sortOrder)
                .build();
    }
}
