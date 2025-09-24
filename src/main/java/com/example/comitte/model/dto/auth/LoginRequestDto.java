package com.example.comitte.model.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class LoginRequestDto {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
}
