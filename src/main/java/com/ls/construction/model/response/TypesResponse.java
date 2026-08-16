package com.ls.construction.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TypesResponse(
        Long id,
        String moduleName,
        String category,
        String typeName,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
}
