package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberResponse(
        Long memberId,
        String username,
        String email,
        String name,
        String mobile,
        String aadharNo,
        String address,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
