package com.example.convergencesoftwarerecruitingbe.domain.form.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.ApplicationSubmitResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.service.ApplicationSubmitService;
import com.example.convergencesoftwarerecruitingbe.domain.form.dto.FormActiveResponse;
import com.example.convergencesoftwarerecruitingbe.domain.form.service.FormQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forms")
public class FormController implements FormControllerDocs {

    private final FormQueryService formQueryService;
    private final ApplicationSubmitService applicationSubmitService;

    @Override
    @GetMapping("/active")
    public ResponseEntity<FormActiveResponse> getActiveForm() {
        FormActiveResponse response = formQueryService.getActiveForm();
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/{formId}/apply")
    public ResponseEntity<ApplicationSubmitResponse> submitApplication(
            @PathVariable Long formId,
            @Valid @RequestBody ApplicationSubmitRequest request
    ) {
        ApplicationSubmitResponse response = applicationSubmitService.submit(formId, request);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    @PostMapping("/active/apply")
    public ResponseEntity<ApplicationSubmitResponse> submitActiveApplication(
            @Valid @RequestBody ApplicationSubmitRequest request
    ) {
        FormActiveResponse activeForm = formQueryService.getActiveForm();
        ApplicationSubmitResponse response = applicationSubmitService.submit(activeForm.getFormId(), request);
        return ResponseEntity.status(201).body(response);
    }
}
