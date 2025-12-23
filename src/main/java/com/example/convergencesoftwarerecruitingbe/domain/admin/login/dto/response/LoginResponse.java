package com.example.convergencesoftwarerecruitingbe.domain.admin.login.dto.response;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.AdminRole;
import com.example.convergencesoftwarerecruitingbe.global.auth.principal.AdminPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String username;
    private AdminRole role;

    public static LoginResponse from(AdminPrincipal principal) {
        return new LoginResponse(principal.getId(), principal.getUsername(), principal.getRole());
    }
}
