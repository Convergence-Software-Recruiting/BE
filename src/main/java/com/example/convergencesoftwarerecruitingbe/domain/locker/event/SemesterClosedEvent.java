package com.example.convergencesoftwarerecruitingbe.domain.locker.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SemesterClosedEvent {
    private final int returnedRentalCount;
    private final LocalDateTime closedAt;
}
