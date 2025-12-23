package com.example.convergencesoftwarerecruitingbe.domain.admin.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyPasswordRequest {

    @NotBlank
    private String currentPassword;
}
