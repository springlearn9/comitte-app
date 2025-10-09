package com.ls.comitte.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
