package com.example.convergencesoftwarerecruitingbe.domain.locker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDecisionRequest {

    @NotBlank
    private String decisionReason;
}
