package com.example.convergencesoftwarerecruitingbe.domain.intro.service;

import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.repository.IntroContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IntroQueryService {

    private final IntroContentRepository introContentRepository;

    @Transactional(readOnly = true)
    public IntroResponse getIntro() {
        return introContentRepository.findTopByOrderByIdAsc()
                .map(IntroResponse::from)
                .orElseGet(IntroResponse::empty);
    }
}
