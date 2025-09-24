package com.example.comitte.model.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class RegisterRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private String mobile;
}
