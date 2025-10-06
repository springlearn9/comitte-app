package com.ls.comitte.model.response;

import java.time.LocalDateTime;

public record ComitteMemberMapResponse(
        Long id,
        Long comitteId,
        Long memberId,
        Integer shareCount,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
