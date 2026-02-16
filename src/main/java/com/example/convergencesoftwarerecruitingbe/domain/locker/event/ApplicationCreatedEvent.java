package com.example.convergencesoftwarerecruitingbe.domain.locker.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationCreatedEvent {
    private final Long applicationId;
    private final String applicantEmail;
    private final String applicantName;
    private final Integer lockerNumber;
}
