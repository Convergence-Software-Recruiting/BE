package com.example.convergencesoftwarerecruitingbe.domain.admin.application.controller;


import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.AdminApplicationMemoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.AdminApplicationStatusRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.service.AdminApplicationService;
import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/applications")
public class AdminApplicationController implements AdminApplicationControllerDocs {

    private final AdminApplicationService adminApplicationService;

    @Override
    @GetMapping
    public ResponseEntity<Page<AdminApplicationListResponse>> list(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<AdminApplicationListResponse> result = adminApplicationService.listApplications(status, pageable);
        return ResponseEntity.ok(result);
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) return Sort.unsorted();

        // "submittedAt,desc" 형태만 처리 (Swagger에서 보통 이렇게 옴)
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = (parts.length > 1)
                ? Sort.Direction.fromString(parts[1].trim())
                : Sort.Direction.ASC;

        return Sort.by(direction, property);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AdminApplicationDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(adminApplicationService.getApplication(id));
    }

    @Override
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AdminApplicationStatusRequest request
    ) {
        adminApplicationService.updateStatus(id, request.getStatus());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/memo")
    public ResponseEntity<Void> updateMemo(
            @PathVariable Long id,
            @Valid @RequestBody AdminApplicationMemoRequest request
    ) {
        adminApplicationService.updateMemo(id, request.getMemo());
        return ResponseEntity.noContent().build();
    }
}