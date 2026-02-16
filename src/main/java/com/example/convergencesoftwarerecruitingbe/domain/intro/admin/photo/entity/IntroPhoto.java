package com.example.convergencesoftwarerecruitingbe.domain.intro.admin.photo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.example.convergencesoftwarerecruitingbe.global.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "intro_photos")
public class IntroPhoto extends BaseTimeEntity {

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

    public static IntroPhoto create(String objectKey, String publicUrl, Integer sortOrder) {
        return IntroPhoto.builder()
                .objectKey(objectKey)
                .publicUrl(publicUrl)
                .sortOrder(sortOrder == null ? 0 : sortOrder)
                .build();
    }

    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
