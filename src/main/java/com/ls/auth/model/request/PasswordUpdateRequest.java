package com.ls.auth.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {
    private String token;
    private String usernameOrEmail;
    private String otp;
    private String newPassword;
}