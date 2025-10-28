package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
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
        LocalDate dob,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
