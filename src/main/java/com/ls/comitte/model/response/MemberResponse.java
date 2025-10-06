package com.ls.comitte.model.response;

import java.time.LocalDateTime;

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
