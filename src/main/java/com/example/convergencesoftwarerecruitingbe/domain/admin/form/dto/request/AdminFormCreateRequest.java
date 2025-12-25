package com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminFormCreateRequest {

    @NotBlank
    private String title;

    private String description;
}
