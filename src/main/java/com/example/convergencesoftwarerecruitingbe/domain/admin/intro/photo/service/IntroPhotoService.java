package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.CreatePhotoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.SyncPhotosRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PhotoResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity.IntroPhoto;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.repository.IntroPhotoRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoUrlBuilder;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoValidation;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntroPhotoService {

    private final IntroPhotoRepository introPhotoRepository;
    private final IntroPhotoUrlBuilder introPhotoUrlBuilder;
    private final IntroPhotoObjectService introPhotoObjectService;

    @Transactional
    public PhotoResponse create(CreatePhotoRequest request) {
        validateCreateRequest(request);

        String publicUrl = introPhotoUrlBuilder.build(request.getObjectKey());

        IntroPhoto photo = introPhotoRepository.save(
                IntroPhoto.create(request.getObjectKey(), publicUrl, request.getSortOrder())
        );

        return PhotoResponse.from(photo);
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> list() {
        return introPhotoRepository.findAllByOrderBySortOrderAscIdAsc()
                .stream()
                .map(PhotoResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long photoId) {
        IntroPhoto photo = introPhotoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));
        introPhotoObjectService.deleteObject(photo.getObjectKey());
        introPhotoRepository.delete(photo);
    }

    @Transactional
    public List<PhotoResponse> sync(SyncPhotosRequest request) {
        List<SyncPhotosRequest.PhotoItem> items = request.getPhotos();
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사진 목록이 비어있습니다");
        }

        List<IntroPhoto> existing = introPhotoRepository.findAllByOrderBySortOrderAscIdAsc();
        Map<String, IntroPhoto> existingByObjectKey = existing.stream()
                .collect(Collectors.toMap(IntroPhoto::getObjectKey, p -> p));

        List<IntroPhoto> toCreate = new java.util.ArrayList<>();
        List<String> resolvedKeys = items.stream()
                .map(this::resolveAndValidateObjectKey)
                .toList();
        Set<String> incomingKeys = resolvedKeys.stream()
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
        if (incomingKeys.size() != resolvedKeys.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 objectKey가 있습니다");
        }

        int index = 0;
        for (String objectKey : incomingKeys) {
            IntroPhoto photo = existingByObjectKey.get(objectKey);
            String publicUrl = introPhotoUrlBuilder.build(objectKey);
            if (photo != null) {
                photo.updateSortOrder(index);
            } else {
                toCreate.add(IntroPhoto.create(objectKey, publicUrl, index));
            }
            index++;
        }

        Set<String> keysToKeep = incomingKeys;
        List<IntroPhoto> toDelete = existing.stream()
                .filter(photo -> !keysToKeep.contains(photo.getObjectKey()))
                .toList();

        if (!toDelete.isEmpty()) {
            introPhotoRepository.deleteAllInBatch(toDelete);
        }
        if (!toCreate.isEmpty()) {
            introPhotoRepository.saveAll(toCreate);
        }
        introPhotoRepository.flush();

        // best-effort object deletion for removed photos
        for (IntroPhoto photo : toDelete) {
            try {
                introPhotoObjectService.deleteObject(photo.getObjectKey());
            } catch (Exception e) {
                log.warn("Failed to delete intro photo object from storage. objectKey={}", photo.getObjectKey(), e);
            }
        }

        return introPhotoRepository.findAllByOrderBySortOrderAscIdAsc()
                .stream()
                .map(PhotoResponse::from)
                .toList();
    }

    private void validateCreateRequest(CreatePhotoRequest request) {
        if (!StringUtils.hasText(request.getObjectKey())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objectKey is required");
        }

        if (!request.getObjectKey().startsWith(IntroPhotoValidation.OBJECT_KEY_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid objectKey format");
        }

        String extension = extractExtension(request.getObjectKey());
        if (extension == null || !IntroPhotoValidation.isAllowedExtension(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file extension");
        }
    }

    private String resolveAndValidateObjectKey(SyncPhotosRequest.PhotoItem item) {
        String objectKey = item.getObjectKey();
        if (!StringUtils.hasText(objectKey)) {
            String publicUrl = item.getPublicUrl();
            if (!StringUtils.hasText(publicUrl)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objectKey 또는 publicUrl이 필요합니다");
            }
            objectKey = extractObjectKeyFromPublicUrl(publicUrl);
        }
        if (!objectKey.startsWith(IntroPhotoValidation.OBJECT_KEY_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid objectKey format");
        }
        String extension = extractExtension(objectKey);
        if (extension == null || !IntroPhotoValidation.isAllowedExtension(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file extension");
        }
        return objectKey;
    }

    private String extractObjectKeyFromPublicUrl(String publicUrl) {
        String baseUrl = introPhotoUrlBuilder.build("");
        if (!publicUrl.startsWith(baseUrl)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "publicUrl이 올바르지 않습니다");
        }
        return publicUrl.substring(baseUrl.length());
    }

    private String extractExtension(String objectKey) {
        int lastDot = objectKey.lastIndexOf('.');
        if (lastDot == -1 || lastDot == objectKey.length() - 1) {
            return null;
        }
        return objectKey.substring(lastDot);
    }
}
