package com.ls.auth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberResponse(
        Long memberId,
        String username,
        String email,
        String name,
        String mobile,
        String aadharNo,
        String address,
        LocalDate dob,
        Set<Long> grantedRoleIds,
        Set<String> grantedRoleNames,
        Set<String> grantedAuthorities,
        Set<GrantedAuthority> allRolesAndAuthorities
) {
    public boolean hasRoleId(final Long roleId) {
        return this.grantedRoleIds != null && this.grantedRoleIds.contains(roleId);
    }

    public boolean hasRole(final String role) {
        return this.grantedRoleNames != null && this.grantedRoleNames.contains(role);
    }

    public boolean hasAuthority(final String authority) {
        return this.grantedAuthorities != null && this.grantedAuthorities.contains(authority);
    }
}