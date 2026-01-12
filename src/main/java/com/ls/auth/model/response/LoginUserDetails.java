package com.ls.auth.model.response;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

@Getter
public class LoginUserDetails extends User {
    private final Long memberId;
    private final String email;
    private final String name;
    private final String mobile;
    private final Set<Long> grantedRoleIds;
    private final Set<String> grantedRoleNames;
    private final Set<String> grantedAuthorities;

    public LoginUserDetails(
            Long memberId,
            String username,
            String email,
            String name,
            String mobile,
            Set<Long> grantedRoleIds,
            Set<String> grantedRoleNames,
            Set<String> grantedAuthorities,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, "", true, true, true, true, authorities);
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.grantedRoleIds = grantedRoleIds;
        this.grantedRoleNames = grantedRoleNames;
        this.grantedAuthorities = grantedAuthorities;
    }
}
