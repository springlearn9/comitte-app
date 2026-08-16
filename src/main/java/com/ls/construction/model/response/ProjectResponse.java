package com.ls.construction.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectResponse(
        Long projectId,
        String name,
        String details,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
}
