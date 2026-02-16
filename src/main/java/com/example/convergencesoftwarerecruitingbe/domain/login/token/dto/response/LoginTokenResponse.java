package com.example.convergencesoftwarerecruitingbe.domain.login.token.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginTokenResponse {
    private String accessToken;
    private String refreshToken;
}
