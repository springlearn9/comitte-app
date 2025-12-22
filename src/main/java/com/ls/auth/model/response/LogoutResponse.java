package com.ls.auth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LogoutResponse(
        String message,
        Long timestamp
) {}
