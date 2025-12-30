package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IntroPhotoUrlBuilder {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket}")
    private String bucket;

    public String build(String objectKey) {
        String normalizedEndpoint = endpoint.endsWith("/") ? endpoint.substring(0, endpoint.length() - 1) : endpoint;
        return normalizedEndpoint + "/" + bucket + "/" + objectKey;
    }

    public String getBucket() {
        return bucket;
    }
}
