package com.example.convergencesoftwarerecruitingbe.domain.intro.service;

import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroSectionResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroResponse;
import com.example.convergencesoftwarerecruitingbe.domain.intro.entity.IntroContent;
import com.example.convergencesoftwarerecruitingbe.domain.intro.repository.IntroContentRepository;
import com.example.convergencesoftwarerecruitingbe.domain.intro.service.command.IntroUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IntroCommandService {

    private final IntroContentRepository introContentRepository;

    @Transactional
    public IntroResponse upsert(IntroUpdateCommand command) {
        IntroSectionResponse about = command.about();
        IntroSectionResponse works = command.works();
        IntroSectionResponse benefits = command.benefits();

        IntroContent content = introContentRepository.findTopByOrderByIdAsc()
                .orElseGet(() -> IntroContent.create(
                        about.title(), about.content(),
                        works.title(), works.content(),
                        benefits.title(), benefits.content()
                ));

        if (content.getId() != null) {
            content.update(
                    about.title(), about.content(),
                    works.title(), works.content(),
                    benefits.title(), benefits.content()
            );
        }

        IntroContent saved = introContentRepository.save(content);
        return IntroResponse.from(saved);
    }
}
