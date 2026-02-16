package com.example.convergencesoftwarerecruitingbe.domain.intro.admin.about.controller;

import com.example.convergencesoftwarerecruitingbe.domain.intro.admin.about.dto.request.AdminIntroAboutUpdateRequest;
import com.example.convergencesoftwarerecruitingbe.domain.intro.client.dto.response.IntroSectionResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.client.dto.response.IntroResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.client.service.IntroCommandService;
import com.example.convergencesoftwarerecruitingbe.domain.intro.client.service.command.IntroUpdateCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/intro/about")
public class AdminIntroAboutController implements AdminIntroAboutControllerDocs {

    private final IntroCommandService introCommandService;

    @Override
    @PutMapping
    public ResponseEntity<IntroResponse> updateIntroAbout(
            @Valid @RequestBody AdminIntroAboutUpdateRequest request
    ) {
        IntroUpdateCommand command = toCommand(request);
        IntroResponse response = introCommandService.upsert(command);
        return ResponseEntity.ok(response);
    }

    private IntroUpdateCommand toCommand(AdminIntroAboutUpdateRequest request) {
        return new IntroUpdateCommand(
                IntroSectionResponse.of(request.getAbout().getTitle(), request.getAbout().getContent()),
                IntroSectionResponse.of(request.getWorks().getTitle(), request.getWorks().getContent()),
                IntroSectionResponse.of(request.getBenefits().getTitle(), request.getBenefits().getContent())
        );
    }
}
