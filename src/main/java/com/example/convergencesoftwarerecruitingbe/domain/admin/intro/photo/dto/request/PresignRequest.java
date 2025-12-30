package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresignRequest {

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;
}
