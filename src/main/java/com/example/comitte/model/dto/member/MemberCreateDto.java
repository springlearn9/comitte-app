package com.example.comitte.model.dto.member;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class MemberCreateDto {
    @NotBlank
    private String username;
    private String email;
    private String name;
    private String mobile;
    private String aadharNo;
    private String address;
}
