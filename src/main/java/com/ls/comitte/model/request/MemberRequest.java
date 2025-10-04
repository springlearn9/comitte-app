package com.ls.comitte.model.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class MemberRequest {
    @NotBlank
    private String username;
    private String email;
    private String name;
    private String mobile;
    private String aadharNo;
    private String address;
}
