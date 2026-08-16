package com.ls.construction.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaterialResponse(
        Long materialId,
        Long projectId,
        String projectName,
        LocalDate materialDate,
        String details,
        String material,
        String materialType,
        String labour,
        String labourType,
        String supplier,
        BigDecimal quantity,
        String unit,
        BigDecimal pricePerUnit,
        BigDecimal amount,
        BigDecimal bhada,
        BigDecimal totalAmount,
        String tags,
        String paymentStatus,
        LocalDate paidDate,
        Long paymentId,
        String paymentDetails,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
}
