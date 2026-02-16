package com.example.convergencesoftwarerecruitingbe.domain.intro.client.controller;

import com.example.convergencesoftwarerecruitingbe.domain.intro.client.dto.response.IntroResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.client.service.IntroQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/intro")
public class IntroController implements IntroControllerDocs {

    private final IntroQueryService introQueryService;

    @GetMapping
    public ResponseEntity<IntroResponse> getIntro() {
        IntroResponse response = introQueryService.getIntro();
        return ResponseEntity.ok(response);
    }
}
