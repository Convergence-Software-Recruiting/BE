package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request;

import com.example.convergencesoftwarerecruitingbe.domain.application.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminApplicationStatusRequest {

    @NotNull
    private ApplicationStatus status;
}
