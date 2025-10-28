package com.ls.comitte.model.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class MemberRequest {
    @NotBlank
    private String username;
    private String email;
    private String name;
    private String mobile;
    private String aadharNo;
    private String password;
    private String address;
    private LocalDate dob;

}
