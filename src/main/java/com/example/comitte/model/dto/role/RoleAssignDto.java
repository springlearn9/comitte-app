package com.example.comitte.model.dto.role;

import lombok.Data;

import java.util.List;

import jakarta.validation.constraints.*;

@Data
public class RoleAssignDto {
    @NotNull
    private List<String> roleNames;
}
