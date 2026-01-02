package com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.CreatePhotoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PhotoResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.PresignRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.response.PresignResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.dto.request.SyncPhotosRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service.IntroPhotoPresignService;
import com.example.convergencesoftwarerecruitingbe.domain.admin.intro.photo.service.IntroPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/intro/photos")
public class AdminIntroPhotoController implements AdminIntroPhotoControllerDocs {

    private final IntroPhotoPresignService introPhotoPresignService;
    private final IntroPhotoService introPhotoService;

    @Override
    @PostMapping("/presign")
    public ResponseEntity<PresignResponse> presign(
            @Valid @RequestBody PresignRequest request
    ) {
        PresignResponse response = introPhotoPresignService.createPresignedUrl(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    @Deprecated
    public ResponseEntity<PhotoResponse> create(
            @Valid @RequestBody CreatePhotoRequest request
    ) {
        PhotoResponse response = introPhotoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long photoId
    ) {
        introPhotoService.delete(photoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping
    public ResponseEntity<List<PhotoResponse>> sync(
            @Valid @RequestBody SyncPhotosRequest request
    ) {
        List<PhotoResponse> responses = introPhotoService.sync(request);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PhotoResponse>> list() {
        List<PhotoResponse> responses = introPhotoService.list();
        return ResponseEntity.ok(responses);
    }
}
