package com.ls.construction.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LabourDetailsResponse(
        Long id,
        Long projectId,
        String projectName,
        LocalDate labourDate,
        String labourType,
        BigDecimal labourAmount,
        String details,
        String tags,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
}
