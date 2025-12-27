package com.example.convergencesoftwarerecruitingbe.domain.admin.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.UpdateApplicationMemoRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request.UpdateApplicationStatusRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationDetailResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.response.AdminApplicationListItemResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.application.service.AdminApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/applications")
public class AdminApplicationController implements AdminApplicationControllerDocs {

    private final AdminApplicationService adminApplicationService;

    @Override
    @GetMapping
    public ResponseEntity<List<AdminApplicationListItemResponse>> list() {
        List<AdminApplicationListItemResponse> responses = adminApplicationService.list();
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{applicationId}")
    public ResponseEntity<AdminApplicationDetailResponse> getDetail(
            @PathVariable Long applicationId
    ) {
        AdminApplicationDetailResponse response = adminApplicationService.getDetail(applicationId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        adminApplicationService.updateStatus(applicationId, request.getStatus());
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/{applicationId}/memo")
    public ResponseEntity<Void> updateMemo(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationMemoRequest request
    ) {
        adminApplicationService.updateMemo(applicationId, request.getAdminMemo());
        return ResponseEntity.ok().build();
    }
}
