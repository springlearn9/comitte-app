package com.ls.construction.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypesRequest {
    @NotBlank
    private String moduleName;
    
    private String category;
    
    @NotBlank
    private String typeName;
}
