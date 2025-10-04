package com.ls.auth.model;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserUpdateDto {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    private String mobileNumber;
}
