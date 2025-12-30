package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.PresignRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PresignResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoUrlBuilder;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoValidation;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IntroPhotoPresignService {

    private static final String OBJECT_KEY_PREFIX = IntroPhotoValidation.OBJECT_KEY_PREFIX;

    private final MinioClient minioClient;
    private final IntroPhotoUrlBuilder introPhotoUrlBuilder;

    @Value("${minio.bucket}")
    private String bucket;

    public PresignResponse createPresignedUrl(PresignRequest request) {
        String fileName = request.getFileName();
        String contentType = request.getContentType();

        if (!StringUtils.hasText(fileName) || !StringUtils.hasText(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fileName and contentType are required");
        }

        String extension = resolveExtension(fileName, contentType);
        String objectKey = OBJECT_KEY_PREFIX + UUID.randomUUID() + extension;

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);

        String uploadUrl = generatePresignedUrl(objectKey, headers);
        String publicUrl = introPhotoUrlBuilder.build(objectKey);

        return PresignResponse.builder()
                .objectKey(objectKey)
                .uploadUrl(uploadUrl)
                .publicUrl(publicUrl)
                .build();
    }

    private String resolveExtension(String fileName, String contentType) {
        String normalizedContentType = IntroPhotoValidation.normalizeContentType(contentType);
        if (!IntroPhotoValidation.isSupportedContentType(normalizedContentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported content type");
        }

        String extensionFromFile = extractExtension(fileName);
        if (extensionFromFile != null) {
            if (!IntroPhotoValidation.isAllowedExtension(extensionFromFile)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file extension");
            }
            if (!IntroPhotoValidation.isExtensionAllowedForContentType(extensionFromFile, normalizedContentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extension does not match contentType");
            }
            return IntroPhotoValidation.normalizeExtension(extensionFromFile);
        }

        String defaultExtension = IntroPhotoValidation.defaultExtensionForContentType(normalizedContentType);
        if (!StringUtils.hasText(defaultExtension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported content type");
        }

        return defaultExtension;
    }

    private String generatePresignedUrl(String objectKey, Map<String, String> headers) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucket)
                            .object(objectKey)
                            .expiry(10, TimeUnit.MINUTES)
                            .extraHeaders(headers)
                            .build()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create presigned URL", e);
        }
    }

    private String extractExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return null;
        }
        String ext = fileName.substring(lastDot);
        return StringUtils.hasText(ext) ? ext : null;
    }
}
