package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoValidation;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class IntroPhotoObjectService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public void deleteObject(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stored objectKey is invalid");
        }
        if (!objectKey.startsWith(IntroPhotoValidation.OBJECT_KEY_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid objectKey format");
        }

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete object from storage", e);
        }
    }
}
