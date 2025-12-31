package com.example.convergencesoftwarerecruitingbe.domain.application.controller;

import com.example.convergencesoftwarerecruitingbe.domain.application.dto.request.ApplicationResultQueryRequest;
import com.example.convergencesoftwarerecruitingbe.domain.application.dto.response.ApplicationResultResponse;
import com.example.convergencesoftwarerecruitingbe.domain.application.service.ApplicationResultQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "application-result-controller", description = "지원 결과 조회 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationResultController implements ApplicationResultControllerDocs {

    private final ApplicationResultQueryService applicationResultQueryService;

    @Override
    @PostMapping("/result")
    public ResponseEntity<ApplicationResultResponse> queryResult(
            @Valid @RequestBody ApplicationResultQueryRequest request
    ) {
        ApplicationResultResponse response = applicationResultQueryService.query(request);
        return ResponseEntity.ok(response);
    }
}
