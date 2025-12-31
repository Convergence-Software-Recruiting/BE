package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.entity.Form;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminFormListItemResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final boolean active;
    private final boolean resultOpen;
    private final LocalDateTime createdAt;

    public static AdminFormListItemResponse from(Form form) {
        return AdminFormListItemResponse.builder()
                .id(form.getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .active(form.isActive())
                .resultOpen(form.isResultOpen())
                .createdAt(form.getCreatedAt())
                .build();
    }
}
