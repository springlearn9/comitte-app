package com.ls.auth.model.response;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn,
    Object user)
{}
