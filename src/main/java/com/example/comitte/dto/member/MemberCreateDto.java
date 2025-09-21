package com.example.comitte.dto.member;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class MemberCreateDto {
    @NotBlank
    private String name;
    private String mobile;
    private String aadharNo;
    private String address;
}
