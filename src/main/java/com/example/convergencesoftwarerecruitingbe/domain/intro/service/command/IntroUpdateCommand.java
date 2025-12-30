package com.example.convergencesoftwarerecruitingbe.domain.intro.service.command;

import com.example.convergencesoftwarerecruitingbe.domain.intro.dto.response.IntroSectionResponse;

public record IntroUpdateCommand(
        IntroSectionResponse about,
        IntroSectionResponse works,
        IntroSectionResponse benefits
) {

    public IntroUpdateCommand {
        about = about == null ? IntroSectionResponse.of("", "") : about;
        works = works == null ? IntroSectionResponse.of("", "") : works;
        benefits = benefits == null ? IntroSectionResponse.of("", "") : benefits;
    }
}
