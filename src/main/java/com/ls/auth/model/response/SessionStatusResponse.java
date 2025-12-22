package com.ls.auth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionStatusResponse(
        boolean active,
        long remainingSeconds,
        String message
) {}
