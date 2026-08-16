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
public class MaterialRequest {
    @NotNull
    private Long projectId;
    
    @NotNull
    private LocalDate materialDate;
    
    private String details;
    private String material;
    private String materialType;
    private String labour;
    private String labourType;
    private String supplier;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal pricePerUnit;
    private BigDecimal amount;
    private BigDecimal bhada;
    private BigDecimal totalAmount;
    private String tags;
    private String paymentStatus;
    private LocalDate paidDate;
    private Long paymentId;
    private String paymentDetails;
}
