package com.example.convergencesoftwarerecruitingbe.global.auth.principal;

import com.example.convergencesoftwarerecruitingbe.domain.admin.login.user.AdminRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@EqualsAndHashCode
public class AdminPrincipal {
    private final Long id;
    private final String username;
    private final AdminRole role;

    public AdminPrincipal(Long id, String username, AdminRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
