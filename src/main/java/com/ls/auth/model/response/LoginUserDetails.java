package com.ls.auth.model.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public record LoginUserDetails(
        Long memberId,
        String username,
        String email,
        String name,
        String mobile,
        Set<Long> grantedRoleIds,
        Set<String> grantedRoleNames,
        Set<String> grantedAuthorities,
        Set<GrantedAuthority> allRolesAndAuthorities
) {
}
