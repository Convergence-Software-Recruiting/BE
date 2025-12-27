package com.example.convergencesoftwarerecruitingbe.domain.admin.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateApplicationMemoRequest {

    @NotNull
    private String adminMemo;
}
