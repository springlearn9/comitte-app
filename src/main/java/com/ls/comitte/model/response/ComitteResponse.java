package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComitteResponse(
        Long comitteId,
        Long ownerId,
        String comitteName,
        LocalDate startDate,
        Integer fullAmount,
        Integer membersCount,
        Integer fullShare,
        Integer dueDateDays,
        Integer paymentDateDays,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
