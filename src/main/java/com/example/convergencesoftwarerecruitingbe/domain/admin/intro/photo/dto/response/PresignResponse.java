package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response;

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
