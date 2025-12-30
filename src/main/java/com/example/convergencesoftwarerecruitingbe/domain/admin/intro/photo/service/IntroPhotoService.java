package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.CreatePhotoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PhotoResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.entity.IntroPhoto;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.repository.IntroPhotoRepository;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoUrlBuilder;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.util.IntroPhotoValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    private String extractExtension(String objectKey) {
        int lastDot = objectKey.lastIndexOf('.');
        if (lastDot == -1 || lastDot == objectKey.length() - 1) {
            return null;
        }
        return objectKey.substring(lastDot);
    }
}
