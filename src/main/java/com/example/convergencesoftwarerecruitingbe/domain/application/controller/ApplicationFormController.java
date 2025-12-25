package com.example.convergencesoftwarerecruitingbe.domain.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationSubmitRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ActiveFormResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationSubmitResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.service.ActiveFormQueryService;
import com.example.convergencesoftwarerecruitingbe.domain.application.service.ApplicationSubmitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "application-form-controller", description = "지원자용 모집/지원 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forms")
public class ApplicationFormController implements ApplicationFormControllerDocs {

    private final ActiveFormQueryService activeFormQueryService;
    private final ApplicationSubmitService applicationSubmitService;

    @Override
    @GetMapping("/active")
    public ResponseEntity<ActiveFormResponse> getActiveForm() {
        ActiveFormResponse response = activeFormQueryService.getActiveForm();
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/active/apply")
    public ResponseEntity<ApplicationSubmitResponse> submitActiveApplication(
            @Valid @RequestBody ApplicationSubmitRequest request
    ) {
        ApplicationSubmitResponse response = applicationSubmitService.submitToActiveForm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
