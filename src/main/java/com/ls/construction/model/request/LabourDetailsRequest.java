package com.ls.construction.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabourDetailsRequest {
    @NotNull
    private Long projectId;
    
    @NotNull
    private LocalDate labourDate;
    
    private String labourType;
    private BigDecimal labourAmount;
    private String details;
    private String tags;
}
