package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComitteMemberMapResponse(
        Long id,
        Long comitteId,
        Long memberId,
        String comitteName,
        String memberName,
        String memberMobile,
        Integer shareCount,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
