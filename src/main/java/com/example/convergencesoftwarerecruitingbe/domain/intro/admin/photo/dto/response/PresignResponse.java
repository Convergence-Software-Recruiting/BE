package com.example.convergencesoftwarerecruitingbe.domain.intro.admin.photo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PresignResponse {

    private final String objectKey;
    private final String uploadUrl;
    private final String publicUrl;
}
