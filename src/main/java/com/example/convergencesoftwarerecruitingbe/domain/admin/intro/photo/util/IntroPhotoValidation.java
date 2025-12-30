package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class IntroPhotoValidation {

    public static final String OBJECT_KEY_PREFIX = "intro/photos/";

    private static final Map<String, List<String>> CONTENT_TYPE_TO_EXTENSIONS = Map.of(
            "image/jpeg", List.of(".jpg", ".jpeg"),
            "image/jpg", List.of(".jpg", ".jpeg"),
            "image/png", List.of(".png"),
            "image/webp", List.of(".webp")
    );

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".jpg", ".jpeg", ".png", ".webp");

    private IntroPhotoValidation() {
    }

    public static String normalizeContentType(String contentType) {
        return contentType == null ? null : contentType.toLowerCase();
    }

    public static String normalizeExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String ext = extension.startsWith(".") ? extension : "." + extension;
        return ext.toLowerCase();
    }

    public static boolean isSupportedContentType(String contentType) {
        return CONTENT_TYPE_TO_EXTENSIONS.containsKey(normalizeContentType(contentType));
    }

    public static boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(normalizeExtension(extension));
    }

    public static boolean isExtensionAllowedForContentType(String extension, String contentType) {
        String normalizedContentType = normalizeContentType(contentType);
        String normalizedExtension = normalizeExtension(extension);
        List<String> allowedExtensions = CONTENT_TYPE_TO_EXTENSIONS.get(normalizedContentType);
        return allowedExtensions != null && allowedExtensions.contains(normalizedExtension);
    }

    public static String defaultExtensionForContentType(String contentType) {
        List<String> allowedExtensions =
                CONTENT_TYPE_TO_EXTENSIONS.get(normalizeContentType(contentType));
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            return null;
        }
        return allowedExtensions.get(0);
    }
}