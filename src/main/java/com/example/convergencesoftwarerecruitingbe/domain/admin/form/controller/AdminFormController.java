package com.example.convergencesoftwarerecruitingbe.domain.admin.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.AdminFormCreateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.request.AdminFormResultOpenRequest;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormListItemResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.dto.response.AdminFormResultOpenResponse;
import com.example.convergencesoftwarerecruitingbe.domain.admin.form.service.AdminFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/forms")
public class AdminFormController implements AdminFormControllerDocs {

    private final AdminFormService adminFormService;

    @Override
    @PostMapping
    public ResponseEntity<AdminFormListItemResponse> create(
            @Valid @RequestBody AdminFormCreateRequest request
    ) {
        AdminFormListItemResponse response = adminFormService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<AdminFormListItemResponse>> list() {
        List<AdminFormListItemResponse> responses = adminFormService.list();
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AdminFormResponse> get(
            @PathVariable Long id
    ) {
        AdminFormResponse response = adminFormService.getForm(id);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {
        adminFormService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate() {
        adminFormService.deactivate();
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/result-open")
    public ResponseEntity<AdminFormResultOpenResponse> updateResultOpen(
            @PathVariable Long id,
            @Valid @RequestBody AdminFormResultOpenRequest request
    ) {
        AdminFormResultOpenResponse response = adminFormService.updateResultOpen(id, request.getResultOpen());
        return ResponseEntity.ok(response);
    }
}
