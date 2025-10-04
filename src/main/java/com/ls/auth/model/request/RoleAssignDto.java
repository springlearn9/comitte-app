package com.ls.auth.model.request;

import lombok.Data;

import java.util.List;

import jakarta.validation.constraints.*;

@Data
public class RoleAssignDto {
    @NotNull
    private List<String> roleNames;
}
